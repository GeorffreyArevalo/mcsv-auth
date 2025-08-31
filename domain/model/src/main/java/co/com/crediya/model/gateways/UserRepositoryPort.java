package co.com.crediya.model.gateways;

import co.com.crediya.model.User;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {

    Mono<User> saveUser(User user);

    Mono<Boolean> existByEmailOrDocument(String email, String document);

    Mono<User> findByDocument(String document);

    Mono<User> findByEmail(String email);

}
