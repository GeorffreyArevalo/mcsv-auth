package co.com.crediya.api.config;

import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.api.rest.user.UserHandler;
import co.com.crediya.api.rest.user.UserRouterRest;
import co.com.crediya.model.User;
import co.com.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class, PathsConfig.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private TransactionalOperator transactionalOperator;

    @MockitoBean
    private UserUseCase userUseCase;

    private final User user = User.builder().
            name("Julian")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .document("10900122")
            .phone("210012312")
            .basePayment(BigDecimal.TEN)
            .build();

    private final UserResponse userResponse = new UserResponse(
            "Julian",
            "Arevalo",
            "arevalo@gmail.com",
            "10900122",
            "210012312",
            LocalDate.now(),
            BigDecimal.TEN
    );



    @BeforeEach
    void setUp() {
        when(userUseCase.findUserByDocument(user.getDocument())).thenReturn(Mono.just(user));
        when( userMapper.modelToResponse(any()) ).thenReturn(userResponse);
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/users/byDocument/{document}", user.getDocument())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}