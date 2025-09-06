package co.com.crediya.security.jwt;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.security.enums.SecurityConstants;

import co.com.crediya.security.util.KeysUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private KeysUtil keysUtil;

    @InjectMocks
    private JwtProvider jwtProvider;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        jwtProvider = new JwtProvider(keysUtil, 60000);

    }

    @Test
    void testGenerateAccessToken() {
        String email = "test@crediya.com";
        String role = "ROLE_ADMIN";

        when(keysUtil.loadPrivateKey()).thenReturn(Mono.just(privateKey));


        Mono<Token> tokenMono = jwtProvider.generateAccessToken(email, role);

        StepVerifier.create(tokenMono)
                .expectNextMatches(token -> token.getEmail().equals(email))
                .verifyComplete();
    }

    @Test
    void testValidateValidToken() {
        String email = "user@crediya.com";
        String role = "ROLE_USER";
        String token = Jwts.builder()
                .subject(email)
                .claim(SecurityConstants.ROLE_CLAIM.getValue(), role)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        when(keysUtil.loadPublicKey()).thenReturn(Mono.just(publicKey));

        Mono<Claims> claimsMono = jwtProvider.validate(token);

        StepVerifier.create(claimsMono)
                .assertNext(claims -> {
                    assert claims.getSubject().equals(email);
                    assert claims.get(SecurityConstants.ROLE_CLAIM.getValue()).equals(role);
                })
                .verifyComplete();
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.value";

        when(keysUtil.loadPublicKey()).thenReturn(Mono.just(publicKey));

        Mono<Claims> claimsMono = jwtProvider.validate(invalidToken);

        StepVerifier.create(claimsMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaUnathorizedException &&
                                throwable.getMessage().equals(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage())
                )
                .verify();
    }
}