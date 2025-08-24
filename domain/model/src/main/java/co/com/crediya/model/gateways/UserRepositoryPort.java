package co.com.crediya.model.gateways;

import co.com.crediya.model.User;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {

    Mono<User> save(User user);

    Mono<User> findByEmail(String email);

}
