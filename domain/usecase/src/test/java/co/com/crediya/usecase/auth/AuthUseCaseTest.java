package co.com.crediya.usecase.auth;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.model.Role;
import co.com.crediya.model.Token;
import co.com.crediya.model.User;
import co.com.crediya.model.gateways.EndpointRepositoryPort;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.port.PasswordEncoderPort;
import co.com.crediya.port.TokenProviderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @Mock
    private EndpointRepositoryPort endpointRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private final User user = User.builder()
            .name("John")
            .lastName("Doe")
            .email("john@doe.com")
            .document("123456")
            .phone("312009212")
            .password("1234")
            .idRole(1L)
            .basePayment( new BigDecimal(10) )
            .build();

    private final Role role = Role.builder()
            .name("Administrador")
            .code("ROLE_ADMIN")
            .description("Description")
            .build();

    private final Token token = Token.builder()
            .accessToken("09991231")
            .typeToken("Bearer")
            .email("john@doe.com")
            .build();


    @Test
    @DisplayName("Must login user successfully")
    void mustLoginUserSuccessfully() {

        when( userRepositoryPort.findByEmail(user.getEmail()) ).thenReturn( Mono.just(user) );
        when( roleRepositoryPort.findById( user.getIdRole() ) ).thenReturn( Mono.just(role) );
        when( tokenProviderPort.generateAccessToken(user.getEmail(), role.getCode() ) ).thenReturn(Mono.just(token));
        when( passwordEncoderPort.verify(user.getPassword(), user.getPassword()) ).thenReturn(Boolean.valueOf("true"));

        Mono<Token> tokenLogin = authUseCase.login(user.getEmail(), user.getPassword());

        StepVerifier.create(tokenLogin)
                .expectNextMatches( tokenResponse -> tokenResponse.getAccessToken().equals(token.getAccessToken()) )
                .verifyComplete();
    }


    @Test
    @DisplayName("Must return error with email no found")
    void mustLoginWithEmailIncorrect() {

        when( userRepositoryPort.findByEmail(user.getEmail()) ).thenReturn( Mono.empty() );

        Mono<Token> tokenLogin = authUseCase.login(user.getEmail(), user.getPassword());

        StepVerifier.create(tokenLogin)
                .expectError(CrediyaUnathorizedException.class)
                .verify();
    }

    @Test
    @DisplayName("Must return error with password does not match")
    void mustLoginWithPasswordNotMatch() {

        when( userRepositoryPort.findByEmail(user.getEmail()) ).thenReturn( Mono.just(user) );
        when( passwordEncoderPort.verify(user.getPassword(), user.getPassword()) ).thenReturn(Boolean.valueOf("false"));

        Mono<Token> tokenLogin = authUseCase.login(user.getEmail(), user.getPassword());

        StepVerifier.create(tokenLogin)
                .expectError(CrediyaUnathorizedException.class)
                .verify();
    }

}
