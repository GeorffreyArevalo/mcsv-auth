package co.com.crediya.handlers.user;

import co.com.crediya.dtos.user.CreateUserRequest;
import co.com.crediya.dtos.user.UserResponse;
import co.com.crediya.mappers.UserMapper;
import co.com.crediya.usecase.user.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserHandlerImpl implements UserHandler {

    private final UserServicePort userServicePort;
    private final UserMapper userMapper;


    @Override
    public Mono<UserResponse> saveUser(CreateUserRequest request) {
        return Mono.just( userMapper.createRequestToModel(request) )
                .flatMap( userServicePort::saveUser )
                .map( userMapper::modelToResponse );
    }
}
