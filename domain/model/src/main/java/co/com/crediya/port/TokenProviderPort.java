package co.com.crediya.port;

import co.com.crediya.model.Token;
import reactor.core.publisher.Mono;

public interface TokenProviderPort {
    Mono<Token> generateAccessToken(String email, String role);
}
