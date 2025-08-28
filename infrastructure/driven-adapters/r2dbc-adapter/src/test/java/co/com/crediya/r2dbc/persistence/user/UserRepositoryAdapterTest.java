package co.com.crediya.r2dbc.persistence.user;

import co.com.crediya.model.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserRepository repository;

    @Mock
    ObjectMapper mapper;

    private User user;

    private UserEntity userEntityOne;
    private UserEntity userEntityTwo;

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

        userEntityOne = UserEntity.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john@doe.com")
                .document("123456")
                .phone("312009212")
                .basePayment( new BigDecimal(10) )
                .build();

        userEntityTwo = UserEntity.builder()
                .id(1L)
                .name("Juana")
                .lastName("Mercedes")
                .email("juana@doe.com")
                .document("123456")
                .phone("312009212")
                .basePayment( new BigDecimal(100) )
                .build();
    }


    @Test
    @DisplayName("Must find user by id")
    void mustFindUserById() {

        when(repository.findById( userEntityOne.getId() ))
                .thenReturn( Mono.just(userEntityOne) );

        when(mapper.map(userEntityOne, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(userEntityOne.getId());

        StepVerifier.create(result)
                .expectNextMatches( userFound -> userFound.getName().equals(user.getName()) )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find user by email")
    void mustFindUserByEmail() {

        when(repository.existsByEmailAndDocument( userEntityOne.getEmail(), userEntityOne.getDocument() ))
                .thenReturn( Mono.just(true) );

        Mono<Boolean> result = repositoryAdapter.existByEmailAndDocument(userEntityOne.getEmail(), userEntityOne.getDocument());

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find user by document")
    void mustFindUserByDocument() {

        when(repository.findByDocument( userEntityOne.getDocument() ))
                .thenReturn( Mono.just(userEntityOne) );

        when(mapper.map(userEntityOne, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByDocument(userEntityOne.getDocument());

        StepVerifier.create(result)
                .expectNextMatches( userFound -> userFound.getDocument().equals(user.getDocument()) )
                .verifyComplete();
    }

    @Test
    @DisplayName("Must find all users")
    void mustFindAllUsers() {

        when(repository.findAll()).thenReturn(Flux.just( userEntityOne, userEntityTwo ) );

        when(mapper.map(userEntityOne, User.class)).thenReturn(user);
        when(mapper.map(userEntityTwo, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Must save user successfully")
    void mustSaveUser() {
        when(repository.save(userEntityOne)).thenReturn(Mono.just(userEntityOne));
        when(mapper.map(userEntityOne, User.class)).thenReturn(user);
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntityOne);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
