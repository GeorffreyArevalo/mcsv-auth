package co.com.crediya.api.rest.user;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.usecase.user.UserServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequest.class)
                .doOnNext( userRequest -> log.info("Saving user={}", userRequest))
                .map( userMapper::createRequestToModel )
                .flatMap( userServicePort::saveUser )
                .map( userMapper::modelToResponse )
                .flatMap( savedUser ->
                            ServerResponse.created(URI.create(""))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue( savedUser )
                ).as( transactionalOperator::transactional );

    }

    public Mono<ServerResponse> listenFindUserByDocument(ServerRequest serverRequest) {
        log.info("Finding user by document");
        String document  = serverRequest.pathVariable("document");

        return userServicePort.findUserByDocument(document)
                .flatMap( user ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue( user )
                );

    }

}
