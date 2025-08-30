package co.com.crediya.usecase.user;


import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserRepositoryPort;
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
class UserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

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

        when( userRepository.existByEmailOrDocument(user.getEmail(), user.getDocument()) ).thenReturn(Mono.just(false));
        when( userRepository.saveUser(user) ).thenReturn(Mono.just(user));

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectNextMatches( savedUser -> savedUser.getDocument().equals(user.getDocument()) )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must returned error if user with document or email already exists")
    void testSaveUserWithEmailOrDocumentAlreadyExists() {

        when( userRepository.existByEmailOrDocument(user.getEmail(), user.getDocument()) ).thenReturn(Mono.just(true));
        when( userRepository.saveUser(user) ).thenReturn(Mono.just(user));

        StepVerifier.create( userUseCase.saveUser(user) )
                .expectError( CrediyaBadRequestException.class )
                .verify();

    }

    @Test
    @DisplayName("Must find user by document")
    void testFindUserByDocument() {

        when( userRepository.findByDocument(user.getDocument()) ).thenReturn(Mono.just(user));

        StepVerifier.create( userUseCase.findUserByDocument(user.getDocument()).log() )
                .expectNextCount( 1 )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return error if not found")
    void testFindUserByDocumentWithNotFound() {

        when( userRepository.findByDocument(user.getDocument()) ).thenReturn(Mono.empty());

        StepVerifier.create( userUseCase.findUserByDocument(user.getDocument()).log() )
                .expectError(CrediyaResourceNotFoundException.class)
                .verify();
    }




}
