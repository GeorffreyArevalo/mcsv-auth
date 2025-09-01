package co.com.crediya.security.jwt;

import co.com.crediya.security.enums.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import io.jsonwebtoken.Claims;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthenticationManagerTest {

    private JwtProvider jwtProvider;
    private JwtAuthenticationManager jwtAuthenticationManager;

    @BeforeEach
    void setUp() {
        jwtProvider = Mockito.mock(JwtProvider.class);
        jwtAuthenticationManager = new JwtAuthenticationManager(jwtProvider);
    }

    @Test
    void authenticate_withValidToken_shouldReturnAuthentication() {
        // Arrange
        String token = "valid-token";
        Claims claims = Mockito.mock(Claims.class);

        when(claims.getSubject()).thenReturn("testUser");
        when(claims.get(SecurityConstants.ROLE_CLAIM.getValue(), String.class)).thenReturn("ADMIN");
        when(jwtProvider.validate(anyString())).thenReturn(Mono.just(claims));

        Authentication authRequest =
                new UsernamePasswordAuthenticationToken("testUser", token);

        // Act & Assert
        StepVerifier.create(jwtAuthenticationManager.authenticate(authRequest))
                .expectNextMatches(auth -> {
                    UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) auth;
                    return authentication.getPrincipal().equals("testUser") &&
                            authentication.getAuthorities().contains(
                                    new SimpleGrantedAuthority(SecurityConstants.PREFIX_ROLE_AUTH.getValue() + "ADMIN")
                            );
                })
                .verifyComplete();

        verify(jwtProvider, times(1)).validate(token);
    }

    @Test
    void authenticate_withInvalidToken_shouldReturnError() {
        // Arrange
        String token = "invalid-token";

        when(jwtProvider.validate(anyString())).thenReturn(Mono.error(new RuntimeException("Invalid token")));

        Authentication authRequest =
                new UsernamePasswordAuthenticationToken("testUser", token);

        // Act & Assert
        StepVerifier.create(jwtAuthenticationManager.authenticate(authRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Invalid token")
                )
                .verify();

        verify(jwtProvider, times(1)).validate(token);
    }
}