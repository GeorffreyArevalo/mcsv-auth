package co.com.crediya.api.rest.auth;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.auth.LoginRequestDTO;
import co.com.crediya.api.dtos.auth.TokenResponseDTO;
import co.com.crediya.api.dtos.user.CreateUserRequestDTO;
import co.com.crediya.api.mappers.TokenMapper;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.Token;
import co.com.crediya.model.User;
import co.com.crediya.usecase.auth.AuthUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        AuthRouterRest.class,
        AuthHandler.class,
        ValidatorUtil.class,
})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class AuthRouterRestTest {

    private static final String LOGIN_PATH = "/api/v1/login";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TokenMapper tokenMapper;

    @MockitoBean
    private AuthUseCase authUseCase;

    @Autowired
    private PathsConfig pathsConfig;

    private final LoginRequestDTO loginRequestDTO = new LoginRequestDTO(
            "georffrey@gmail.com", "123"
    );

    private final Token token = Token.builder()
            .email("georffrey@gmail.com")
            .typeToken("Bearer")
            .accessToken("123456")
            .build();

    private final TokenResponseDTO tokenResponseDTO =  new TokenResponseDTO(
            "Bearer", "123456", "georffrey@gmail.com"
    );

    @Test
    void shouldLoadUserPathPathProperties() {
        assertEquals(LOGIN_PATH, pathsConfig.getAuthLogin());
    }

    @Test
    @DisplayName("Must login successfully")
    void testListenSaveUser() {

        when( authUseCase.login(loginRequestDTO.email(), loginRequestDTO.password()) ).thenReturn(Mono.just(token));
        when( tokenMapper.modelToResponse( any(Token.class) ) ).thenReturn(tokenResponseDTO);

        webTestClient.post()
                .uri(LOGIN_PATH)
                .bodyValue(loginRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.OK.status())
                .jsonPath("$.data.accessToken").isEqualTo(token.getAccessToken());
    }


}
