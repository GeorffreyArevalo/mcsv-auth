package co.com.crediya.handlers.user;

import co.com.crediya.dtos.user.CreateUserRequest;
import co.com.crediya.dtos.user.UserResponse;
import reactor.core.publisher.Mono;

public interface UserHandler {

    Mono<UserResponse> saveUser(CreateUserRequest request );

}
