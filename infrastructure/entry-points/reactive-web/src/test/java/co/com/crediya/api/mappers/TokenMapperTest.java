package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.auth.TokenResponseDTO;
import co.com.crediya.model.Token;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TokenMapperTest {

    private final TokenMapper tokenMapper = Mappers.getMapper(TokenMapper.class);

    private final Token token = Token.builder()
            .email("georffrey@gmail.com")
            .typeToken("Bearer")
            .accessToken("123456")
            .build();


    @Test
    void testTokenToResponse() {

        Mono<TokenResponseDTO> result = Mono.fromCallable(() -> tokenMapper.modelToResponse(token));

        StepVerifier.create(result)
                .expectNextMatches( tokenResponse ->
                        tokenResponse.accessToken().equals(token.getAccessToken())
                                &&  tokenResponse.email().equals(token.getEmail())
                                &&  tokenResponse.typeToken().equals(token.getTypeToken())
                )
                .verifyComplete();

    }

}
