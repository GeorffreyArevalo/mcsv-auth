package co.com.crediya.api.rest.user;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.api.util.HandlersUtil;
import co.com.crediya.usecase.user.UserServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;
    private final Validator validator;
    private final HandlersUtil handlersUtil;


    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequest.class)
                .doOnNext( userRequest -> log.info("Saving user={}", userRequest))
                .flatMap( userRequest -> {
                    Errors errors = new BeanPropertyBindingResult(userRequest, CreateUserRequest.class.getName());
                    validator.validate(userRequest, errors);

                    if( errors.hasErrors() ) return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON).bodyValue(handlersUtil.buildBodyResponse(
                                    false, HttpStatus.BAD_REQUEST.value(), "error", handlersUtil.getFieldErrors(errors)
                            ));

                    return Mono.just(userRequest)
                            .map( userMapper::createRequestToModel )
                            .flatMap( userServicePort::saveUser )
                            .map( userMapper::modelToResponse )
                            .flatMap( savedUser ->
                                    ServerResponse.created(URI.create(""))
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue( handlersUtil.buildBodyResponse(true, HttpStatus.CREATED.value(), "data", savedUser) )
                            );
                } );


    }

    public Mono<ServerResponse> listenFindUserByDocument(ServerRequest serverRequest) {
        log.info("Finding user by document");
        String document  = serverRequest.pathVariable("document");

        return userServicePort.findUserByDocument(document)
                .flatMap( user ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue( this.handlersUtil.buildBodyResponse(true, HttpStatus.OK.value(), "data", user) )
                );

    }

}
