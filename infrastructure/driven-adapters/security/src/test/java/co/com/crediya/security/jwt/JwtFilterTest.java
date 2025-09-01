package co.com.crediya.security.jwt;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.security.enums.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter jwtFilter;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter();
        chain = mock(WebFilterChain.class);
        when(chain.filter(Mockito.any())).thenReturn(Mono.empty());
    }

    @Test
    void mustAllowLoginPathWithoutAuthorization() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/auth/login").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> result = jwtFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .expectError(CrediyaUnathorizedException.class)
                .verify();
    }

    @Test
    void mustThrowWhenAuthorizationHeaderMissing() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/protected").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> result = jwtFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaUnathorizedException &&
                                throwable.getMessage().equals(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())
                )
                .verify();
    }

    @Test
    void mustThrowWhenAuthorizationHeaderInvalidFormat() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, "InvalidTokenFormat")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> result = jwtFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaUnathorizedException &&
                                throwable.getMessage().equals(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())
                )
                .verify();
    }

    @Test
    void mustStoreTokenAndContinueWhenAuthorizationHeaderValid() {
        String token = "valid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/protected")
                .header(HttpHeaders.AUTHORIZATION, SecurityConstants.REGEX_TYPE_TOKEN.getValue() + token)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> result = jwtFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

    }
}
