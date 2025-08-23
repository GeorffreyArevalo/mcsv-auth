package co.com.crediya.api.rest.user;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.usecase.user.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequest.class)
                .map( userMapper::createRequestToModel )
                .flatMap( userServicePort::saveUser )
                .map( userMapper::modelToResponse )
                .flatMap( savedUser ->
                            ServerResponse.created(URI.create(""))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(savedUser)
                        ).as(
                                transactionalOperator::transactional
                );

    }

}
