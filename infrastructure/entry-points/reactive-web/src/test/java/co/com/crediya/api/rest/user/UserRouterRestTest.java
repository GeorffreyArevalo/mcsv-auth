package co.com.crediya.api.rest.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.config.UserTestConfiguration;
import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.model.User;
import co.com.crediya.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import  static org.mockito.Mockito.when;

import java.math.BigDecimal;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
@Import(UserTestConfiguration.class)
class UserRouterRestTest {

    private static final String USERS_PATH = "/api/v1/users";
    private static final String USERS_PATH_ID = "/api/v1/users/{id}";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @Autowired
    private TransactionalOperator  transactionalOperator;


    private CreateUserRequest createUserRequest;
    private User user;

    @Autowired
    private PathsConfig pathsConfig;

    @BeforeEach
    void setUp() {


        createUserRequest = new CreateUserRequest(
                "Julian",
                "Arevalo",
                "arevalo@gmail.com",
                "10900122",
                "210012312",
                BigDecimal.TEN
        );

        user = User.builder().
            name("Julian")
                .lastName("Arevalo")
                .email("arevalo@gmail.com")
                .document("10900122")
                .phone("210012312")
                .basePayment(BigDecimal.TEN)
                .build();
    }

    @Test
    void shouldLoadUserPathPathProperties() {
        assertEquals(USERS_PATH, pathsConfig.getUsers());
        assertEquals(USERS_PATH_ID, pathsConfig.getUsersById());
    }

    @Test
    void testListeSaveUser() {

        when( userUseCase.saveUser(user) ).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri(USERS_PATH)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isNotNull();
                        }
                );
    }
}
