package co.com.crediya.api.rest.user;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.user.CreateUserRequest;
import co.com.crediya.api.dtos.user.UserResponse;
import co.com.crediya.api.mappers.UserMapper;
import co.com.crediya.model.User;
import co.com.crediya.ports.TransactionManagement;
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
import static org.mockito.Mockito.any;

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
    private UserMapper userMapper;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private TransactionManagement transactionManagement;

    @Autowired
    private PathsConfig pathsConfig;

    private final UserResponse userResponse = new UserResponse(
            "Julian",
            "Arevalo",
            "arevalo@gmail.com",
            "10900122",
            "210012312",
            LocalDate.now(),
            BigDecimal.TEN
    );

    private final CreateUserRequest createUserRequest = new CreateUserRequest(
            "Julian",
            "Arevalo",
            "arevalo@gmail.com",
            "10900122",
            "210012312",
            LocalDate.now(),
            BigDecimal.TEN
    );

    private final CreateUserRequest createBadUserRequest = new CreateUserRequest(
            "",
            "Arevalo",
            "arevalo@gm",
            "10900122",
            "210012312",
            LocalDate.now(),
            BigDecimal.ZERO
    );

    private final User user = User.builder().
            name("Julian")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .document("10900122")
            .phone("210012312")
            .basePayment(BigDecimal.TEN)
            .build();

    private final User badUser = User.builder().
            name("")
            .lastName("Arevalo")
            .email("arevalo@gmail.com")
            .document("10900122")
            .phone("210012312")
            .basePayment(BigDecimal.TEN)
            .build();

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
        when( userMapper.modelToResponse( any(User.class) ) ).thenReturn( userResponse );
        when( userMapper.createRequestToModel( any( CreateUserRequest.class ) ) ).thenReturn( user );

        when(transactionManagement.inTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        webTestClient.post()
                .uri(USERS_PATH)
                .bodyValue(createUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.statusCode").isEqualTo(201)
                .jsonPath("$.data.document").isEqualTo(user.getDocument())
                .jsonPath("$.data.email")
                .value( email -> {
                    Assertions.assertThat(email).isEqualTo(user.getEmail());

                } );
    }

    @Test
    @DisplayName("Must return error when save a user.")
    void testListenSaveUserWithError() {

        when( userUseCase.saveUser(any(User.class)) ).thenReturn( Mono.just(user) );
        when( userMapper.modelToResponse( any(User.class) ) ).thenReturn( userResponse );
        when( userMapper.createRequestToModel( any( CreateUserRequest.class ) ) ).thenReturn( badUser );


        webTestClient.post()
                .uri(USERS_PATH)
                .bodyValue(createBadUserRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.statusCode").isEqualTo(400);
    }

    @Test
    @DisplayName("Must find user with document")
    void testListenFindUserByDocument() {

        when( userUseCase.findUserByDocument(user.getDocument()) ).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri(USERS_PATH_BY_DOCUMENT, createUserRequest.document())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.statusCode").isEqualTo(200)
                .jsonPath("$.data.name").isEqualTo(user.getName())
                .jsonPath("$.data.email")
                .value( email -> {
                    Assertions.assertThat(email).isEqualTo(user.getEmail());
                } );

    }
}
