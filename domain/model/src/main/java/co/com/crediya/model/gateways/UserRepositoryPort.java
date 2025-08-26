package co.com.crediya.model.gateways;

import co.com.crediya.model.User;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {

    Mono<User> saveUser(User user);

    Mono<User> findByEmail(String email);

    Mono<User> findByDocument(String document);

}
