package co.com.crediya.r2dbc.persistence.user;

import co.com.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmailOrDocument(String email, String document);
    Mono<UserEntity> findByDocument(String document);
    Mono<UserEntity> findByEmail(String email);

}
