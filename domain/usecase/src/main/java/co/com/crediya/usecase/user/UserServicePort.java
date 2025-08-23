package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserServicePort {

    Mono<User> saveUser(User user);

}
