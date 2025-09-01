package co.com.crediya.security.repository;
import co.com.crediya.security.enums.SecurityConstants;
import co.com.crediya.security.jwt.JwtAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecurityContextRepositoryTest {

    private JwtAuthenticationManager jwtAuthenticationManager;
    private ServerSecurityContextRepository securityContextRepository;

    @BeforeEach
    void setUp() {
        jwtAuthenticationManager = Mockito.mock(JwtAuthenticationManager.class);
        securityContextRepository = new SecurityContextRepository(jwtAuthenticationManager);
    }

    @Test
    void load_withValidToken_shouldReturnSecurityContext() {

        String token = "valid-token";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testUser", null);

        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getAttributes().put(SecurityConstants.TOKEN_CLAIM.getValue(), token);

        when(jwtAuthenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(Mono.just(authentication));


        StepVerifier.create(securityContextRepository.load(exchange))
                .expectNextMatches(securityContext ->
                        securityContext.getAuthentication().getPrincipal().equals("testUser")
                )
                .verifyComplete();

        verify(jwtAuthenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    @Test
    void load_withoutToken_shouldReturnEmpty() {

        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(securityContextRepository.load(exchange))
                .verifyComplete();

        verify(jwtAuthenticationManager, never()).authenticate(any(Authentication.class));
    }

    @Test
    void load_withInvalidToken_shouldReturnError() {

        String token = "invalid-token";

        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getAttributes().put(SecurityConstants.TOKEN_CLAIM.getValue(), token);

        when(jwtAuthenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(Mono.error(new RuntimeException("Invalid token")));

        StepVerifier.create(securityContextRepository.load(exchange))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Invalid token")
                )
                .verify();

        verify(jwtAuthenticationManager, times(1)).authenticate(any(Authentication.class));
    }
}