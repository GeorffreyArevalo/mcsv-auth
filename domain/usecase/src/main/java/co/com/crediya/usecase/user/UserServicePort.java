package co.com.crediya.usecase.user;

import co.com.crediya.model.User;
import reactor.core.publisher.Mono;

public interface UserServicePort {

    Mono<User> saveUser(User user);

    Mono<User> findUserByDocument( String document );

}
