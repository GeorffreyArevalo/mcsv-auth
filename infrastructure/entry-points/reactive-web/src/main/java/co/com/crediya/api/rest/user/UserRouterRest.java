package co.com.crediya.api.rest.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.api.exception.model.CustomError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UserRouterRest {

    private final PathsConfig pathsConfig;



    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE,
                    },
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation( tags = "Users", operationId = "saveUser", description = "Save a user", summary = "Save a user",
                            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateUserRequest.class ) ) ),
                            responses = { @ApiResponse( responseCode = "201", description = "User saved successfully.", content = @Content( schema = @Schema( implementation = UserResponse.class ) ) ),
                                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/byDocument/{document}",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE,
                    },
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindUserByDocument",
                    operation = @Operation( tags = "Users", operationId = "findUserByDocument", description = "Find a user by document", summary = "Find a user by document",
                            parameters = {
                                @Parameter( in = ParameterIn.PATH, name = "document", description = "User document", required = true, example = "1200812" )
                            },
                            responses = { @ApiResponse( responseCode = "200", description = "User found.", content = @Content( schema = @Schema( implementation = UserResponse.class ) ) ),
                                    @ApiResponse( responseCode = "404", description = "User with document not found.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST(pathsConfig.getUsers()), handler::listenSaveUser)
                .andRoute(GET(pathsConfig.getUsersByDocument()),  handler::listenFindUserByDocument);
    }
}
