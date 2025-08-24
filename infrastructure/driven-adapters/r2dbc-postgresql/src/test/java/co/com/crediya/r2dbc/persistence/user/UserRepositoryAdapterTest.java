package co.com.crediya.r2dbc.persistence.user;

import co.com.crediya.model.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
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

    private UserEntity userEntity1;
    private UserEntity userEntity2;

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

        userEntity1 = UserEntity.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john@doe.com")
                .document("123456")
                .phone("312009212")
                .basePayment( new BigDecimal(10) )
                .build();

        userEntity2 = UserEntity.builder()
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
    void mustFindValueById() {

        when(repository.findById( userEntity1.getId() ))
                .thenReturn( Mono.just(userEntity1) );

        when(mapper.map(userEntity1, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(userEntity1.getId());

        StepVerifier.create(result)
                .expectNextMatches( userFound -> userFound.getName().equals(user.getName()) )
                .verifyComplete();
    }

    @Test
    void mustFindValueByEmail() {

        when(repository.findByEmail( userEntity1.getEmail() ))
                .thenReturn( Mono.just(userEntity1) );

        when(mapper.map(userEntity1, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByEmail(userEntity1.getEmail());

        StepVerifier.create(result)
                .expectNextMatches( userFound -> userFound.getEmail().equals(user.getEmail()) )
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {

        when(repository.findAll()).thenReturn(Flux.just( userEntity1, userEntity2 ) );

        when(mapper.map(userEntity1, User.class)).thenReturn(user);
        when(mapper.map(userEntity2, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save(userEntity1)).thenReturn(Mono.just(userEntity1));
        when(mapper.map(userEntity1, User.class)).thenReturn(user);
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity1);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
