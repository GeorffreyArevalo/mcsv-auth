package co.com.crediya.api.rest.user;

import co.com.crediya.api.dtos.CrediyaResponseDTO;
import co.com.crediya.api.dtos.user.CreateUserRequestDTO;
import co.com.crediya.api.dtos.user.UserResponseDTO;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;
    private final ValidatorUtil validatorUtil;



    @Operation( tags = "Users", operationId = "saveUser", description = "Save a user", summary = "Save a user",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateUserRequestDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "User saved successfully.", content = @Content( schema = @Schema( implementation = UserResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "401", description = "Unauthorized.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "403", description = "Access Denied.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequestDTO.class)
                .doOnNext( userRequest -> log.info("Saving user={}", userRequest))
                .flatMap( validatorUtil::validate )
                .map( userMapper::createRequestToModel )
                .flatMap( userUseCase::saveUser )
                .map( userMapper::modelToResponse )
                .flatMap( savedUser ->
                        ServerResponse.created(URI.create(""))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue( HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedUser) )
                );

    }


    @Operation( tags = "Users", operationId = "findUserByDocument", description = "Find a user by document", summary = "Find a user by document",
            parameters = {
                    @Parameter( in = ParameterIn.PATH, name = "document", description = "User document", required = true, example = "1200812" ),
            },
            responses = { @ApiResponse( responseCode = "200", description = "User found.", content = @Content( schema = @Schema( implementation = UserResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "404", description = "User with document not found.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "401", description = "Unauthorized.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "403", description = "Access Denied.", content = @Content( schema = @Schema( implementation = CrediyaResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenFindUserByDocument(ServerRequest serverRequest) {
        log.info("Finding user by document");
        String document  = serverRequest.pathVariable("document");
        return userUseCase.findUserByDocument(document)
                .map( userMapper::modelToResponse )
                .flatMap( userResponse ->
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue( HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.OK.status(), userResponse) )
                );

    }

}
