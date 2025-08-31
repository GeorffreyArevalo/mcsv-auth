package co.com.crediya.security.jwt;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Token;
import co.com.crediya.port.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider implements TokenProviderPort {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Integer expiration;

    public Mono<Token> generateAccessToken(String email, String role) {

        return Mono.just(
            Token.builder()
                .email(email)
                .typeToken("Bearer")
                .accessToken(
                    Jwts.builder()
                        .subject(email)
                        .claim("role", role)
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(getSecretKey(secret))
                        .compact()
                ).build()
        );
    }

    public Mono<Claims> validate(String token) {

        return Mono.fromCallable(() -> Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
            ).onErrorResume( error ->
                Mono.error(new CrediyaUnathorizedException(ExceptionMessages.UNAUTHORIZED_SENT_TOKEN_INVALID.getMessage()))
            );
    }

    private SecretKey getSecretKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


}

