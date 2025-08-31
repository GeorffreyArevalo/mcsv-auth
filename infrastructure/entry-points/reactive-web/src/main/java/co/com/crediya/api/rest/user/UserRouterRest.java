package co.com.crediya.api.rest.user;

import co.com.crediya.api.config.PathsConfig;
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
            @RouterOperation(path = "/api/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE,}, method = RequestMethod.POST, beanClass = UserHandler.class, beanMethod = "listenSaveUser"),
            @RouterOperation(path = "/api/v1/users/byDocument/{document}", produces = { MediaType.APPLICATION_JSON_VALUE, }, method = RequestMethod.GET, beanClass = UserHandler.class, beanMethod = "listenFindUserByDocument")
    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
        return route(POST(pathsConfig.getUsers()), handler::listenSaveUser)
                .andRoute(GET(pathsConfig.getUsersByDocument()),  handler::listenFindUserByDocument);
    }
}
