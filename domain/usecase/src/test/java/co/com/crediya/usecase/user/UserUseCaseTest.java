package co.com.crediya.usecase.user;


import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.ports.CrediyaLoggerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private CrediyaLoggerPort crediyaLoggerPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john@doe.com")
                .document("123456")
                .phone("312009212")
                .basePayment( new BigDecimal(10) )
                .build();
    }


    @Test
    @DisplayName("Must save a user successfully")
    void testSaveUser() {

        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.empty());
        when( userRepository.save(user) ).thenReturn(Mono.just(user));

        StepVerifier.create( userUseCase.saveUser(user).log() )
                .expectNext( user )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must returned error if email already exists")
    void testSaveUserWithEmailAlreadyExists() {

        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.just(user));

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaBadRequestException.class )
                .verify();

    }


    @Test
    @DisplayName("Must returned error if name is empty or null")
    void testSaveUserWithNameNullOrEmpty() {

        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.empty());

        user.setName("");

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must returned error if last name is empty or null")
    void testSaveUserWithLastNameNullOrEmpty() {

        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.empty());

        user.setLastName("");

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must returned error if email is invalid")
    void testSaveUserWithInvalidEmail() {

        user.setEmail("georffrey");
        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.empty());

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }

    @Test
    @DisplayName("Must returned error if base payment is out of range")
    void testSaveUserWithPaymentOutOfRange() {

        user.setBasePayment( new BigDecimal(0) );
        when( userRepository.findByEmail(user.getEmail()) ).thenReturn(Mono.empty());

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaIllegalArgumentException.class )
                .verify();

    }






}
