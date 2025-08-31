package co.com.crediya.api.rest.auth;

import co.com.crediya.api.config.PathsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class AuthRouterRest {

    private final PathsConfig pathsConfig;

    @Bean
    public RouterFunction<ServerResponse> authRouterFunction( AuthHandler handler ) {
        return route( POST( pathsConfig.getAuthLogin()), handler::listenLogin );
    }

}
