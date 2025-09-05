package co.com.crediya.port;

import co.com.crediya.model.Token;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenProviderPort {
    Mono<Token> generateAccessToken(String email, String role);
}
