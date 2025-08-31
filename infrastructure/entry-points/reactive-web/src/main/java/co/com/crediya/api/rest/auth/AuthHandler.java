package co.com.crediya.api.rest.auth;


import co.com.crediya.api.dtos.auth.LoginRequestDTO;
import co.com.crediya.api.mappers.TokenMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.auth.AuthUseCase;
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

    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .flatMap( validatorUtil::validate )
                .flatMap( loginRequest -> authUseCase.login( loginRequest.email(), loginRequest.password() ) )
                .map( tokenMapper::modelToResponse )
                .flatMap( tokenResponse ->
                    ServerResponse.ok()
                            .contentType( MediaType.APPLICATION_JSON )
                        .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.OK.status(), tokenResponse))
                );


    }


}
