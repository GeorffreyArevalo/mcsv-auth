package co.com.crediya.api.rest.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.model.User;
import co.com.crediya.usecase.user.UserUseCase;
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
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class UserRouterRestTest {

    private static final String USERS_PATH = "/api/v1/users";
    private static final String USERS_PATH_ID = "/api/v1/users/{id}";
    private static final String USERS_PATH_BY_DOCUMENT = "/api/v1/users/byDocument/{document}";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;


    private final CreateUserRequest createUserRequest = new CreateUserRequest(
            "Julian",
            "Arevalo",
            "arevalo@gmail.com",
            "10900122",
            "210012312",
            LocalDate.now(),
            BigDecimal.TEN
    );

    private final User user = User.builder().
            name("Julian")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .document("10900122")
            .phone("210012312")
            .basePayment(BigDecimal.TEN)
            .build();

    @Autowired
    private PathsConfig pathsConfig;

    @Test
    void shouldLoadUserPathPathProperties() {
        assertEquals(USERS_PATH, pathsConfig.getUsers());
        assertEquals(USERS_PATH_ID, pathsConfig.getUsersById());
        assertEquals(USERS_PATH_BY_DOCUMENT, pathsConfig.getUsersByDocument());
    }

    @Test
    @DisplayName("Must save a user successfully")
    void testListenSaveUser() {

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

    @Test
    @DisplayName("Must find user with document")
    void testListenFindUserByDocument() {

        when( userUseCase.findUserByDocument(user.getDocument()) ).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri(USERS_PATH_BY_DOCUMENT, createUserRequest.document())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value( userResponse -> {
                    Assertions.assertThat( userResponse.document() ).isEqualTo( user.getDocument() );
                } );

    }
}
