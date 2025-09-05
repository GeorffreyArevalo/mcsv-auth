package co.com.crediya.api.rest.auth;


import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.dtos.auth.LoginRequestDTO;
import co.com.crediya.api.dtos.auth.TokenResponseDTO;
import co.com.crediya.api.mappers.SearchParamsMapper;
import co.com.crediya.api.mappers.TokenMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.auth.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHandler {

    private final AuthUseCase authUseCase;
    private final ValidatorUtil validatorUtil;
    private final TokenMapper tokenMapper;

    private final SearchParamsMapper searchParamsMapper;

    @Operation( tags = "Auth", operationId = "login", description = "Login in application", summary = "Login in application",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = LoginRequestDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "200", description = "Login successfully.", content = @Content( schema = @Schema( implementation = TokenResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "401", description = "Unauthorized.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .doOnNext( loginRequestDTO -> log.info("Received login request {}", loginRequestDTO) )
                .flatMap( validatorUtil::validate )
                .flatMap( loginRequest -> authUseCase.login( loginRequest.email(), loginRequest.password() ) )
                .map( tokenMapper::modelToResponse )
                .flatMap( tokenResponse ->
                    ServerResponse.ok()
                            .contentType( MediaType.APPLICATION_JSON )
                        .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.OK.status(), tokenResponse))
                );


    }

    @Operation( tags = "Auth", operationId = "roleHasPermissions", description = "Check if a role has permission to endpoint", summary = "Check if a role has permission to endpoint",
            responses = { @ApiResponse( responseCode = "200", description = "Check successfully.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "401", description = "Unauthorized.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenRoleHasPermissions(ServerRequest serverRequest) {


        return Mono.just(serverRequest.queryParams())
            .map(searchParamsMapper::queryParamsToCheckRolePermissionDTO)
            .flatMap(validatorUtil::validate)
            .flatMap( searchParams -> authUseCase.roleHasPermission(searchParams.roleCode(), searchParams.path(), searchParams.method()) )
            .flatMap( exists ->
                ServerResponse.ok()
                    .contentType( MediaType.APPLICATION_JSON )
                    .bodyValue( HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.OK.status(), exists) )
            );
    }


}
