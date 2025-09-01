package co.com.crediya.security.jwt;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.security.enums.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class JwtFilter implements WebFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just( exchange.getRequest() )
                .filter( request ->
                        !request.getURI().getPath().equals("/api/v1/auth/login")  && !request.getURI().getPath().startsWith("/openapi/")
                )
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .filter( request -> Objects.nonNull(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
                .switchIfEmpty( Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())) )
                .map( request -> request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION) )
                .filter( authorization -> authorization.startsWith(SecurityConstants.REGEX_TYPE_TOKEN.getValue()) )
                .switchIfEmpty(Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())))
                .map(authorization -> authorization.replace(SecurityConstants.REGEX_TYPE_TOKEN.getValue(), ""))
                .doOnNext( token -> exchange.getAttributes().put(SecurityConstants.TOKEN_CLAIM.getValue(), token))
                .then(chain.filter(exchange));
    }
}


