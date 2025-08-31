package co.com.crediya.security.jwt;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.port.TokenProviderPort;
import co.com.crediya.security.util.KeysUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider implements TokenProviderPort {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Integer expiration;

    private final KeysUtil keysUtil;

    public Mono<Token> generateAccessToken(String email, String role) {

        return keysUtil.loadPrivateKey()
                        .map( privateKey ->
                            Token.builder()
                                .email(email)
                                .typeToken("Bearer")
                                .accessToken(
                                    Jwts.builder()
                                        .subject(email)
                                        .claim("role", role)
                                        .issuedAt(new Date())
                                        .expiration(new Date(System.currentTimeMillis() + expiration))
                                        .signWith( privateKey, Jwts.SIG.RS256 )
                                        .compact()
                                ).build()
                        );

    }

    public Mono<Claims> validate(String token) {

        return  keysUtil.loadPublicKey()
                .map(publicKey ->
                    Jwts.parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                ).onErrorResume( error ->
                    Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage()))
                );
    }


}

