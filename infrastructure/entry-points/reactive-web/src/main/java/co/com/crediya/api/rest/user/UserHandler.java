package co.com.crediya.api.rest.user;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.api.exception.model.CustomError;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.api.util.HandlersUtil;
import co.com.crediya.ports.TransactionManagement;
import co.com.crediya.usecase.user.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;
    private final Validator validator;

    private final TransactionManagement transactionManagement;



    @Operation( tags = "Users", operationId = "saveUser", description = "Save a user", summary = "Save a user",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateUserRequest.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "User saved successfully.", content = @Content( schema = @Schema( implementation = UserResponse.class ) ) ),
                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
            }
    )
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequest.class)
                .doOnNext( userRequest -> log.info("Saving user={}", userRequest))
                .flatMap( userRequest -> {
                    Errors errors = HandlersUtil.validateRequestsErrors(userRequest, CreateUserRequest.class.getName(), validator);
                    if( errors.hasErrors() ) return HandlersUtil.buildBadRequestResponse(errors);
                    return Mono.just(userRequest)
                            .map( userMapper::createRequestToModel )
                            .flatMap( saveUser -> transactionManagement.inTransaction(userServicePort.saveUser(saveUser)) )
                            .map( userMapper::modelToResponse )
                            .flatMap( savedUser ->
                                    ServerResponse.created(URI.create(""))
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue( HandlersUtil.buildBodyResponse(true, HttpStatus.CREATED.value(), "data", savedUser) )
                            );
                } );


    }


    @Operation( tags = "Users", operationId = "findUserByDocument", description = "Find a user by document", summary = "Find a user by document",
            parameters = {
                    @Parameter( in = ParameterIn.PATH, name = "document", description = "User document", required = true, example = "1200812" )
            },
            responses = { @ApiResponse( responseCode = "200", description = "User found.", content = @Content( schema = @Schema( implementation = UserResponse.class ) ) ),
                    @ApiResponse( responseCode = "404", description = "User with document not found.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
            }
    )
    public Mono<ServerResponse> listenFindUserByDocument(ServerRequest serverRequest) {
        log.info("Finding user by document");
        String document  = serverRequest.pathVariable("document");
        return userServicePort.findUserByDocument(document)
                .flatMap( user ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue( HandlersUtil.buildBodyResponse(true, HttpStatus.OK.value(), "data", user) )
                );

    }

}
