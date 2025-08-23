package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.model.user.gateways.UserRepositoryPort;
import reactor.core.publisher.Mono;

public class UserUseCase implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> saveUser(User user) {

        return userRepository.findByEmail(user.getEmail())
                .flatMap( savedUser ->

                        Mono.error( new CrediyaBadRequestException(
                                String.format(ExceptionMessages.USER_WITH_EMAIL_EXIST.getMessage(),  user.getEmail())
                        ))
                ).
                switchIfEmpty( UserValidator.validateSaveUser(user) )
                .flatMap( validUser -> userRepository.save(user) );
    }
}
