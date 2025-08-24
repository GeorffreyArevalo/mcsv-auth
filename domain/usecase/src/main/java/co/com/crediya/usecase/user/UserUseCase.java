package co.com.crediya.usecase.user;

import co.com.crediya.exceptions.CrediyaIllegalArgumentException;
import co.com.crediya.model.User;
import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.ports.CrediyaLoggerPort;
import reactor.core.publisher.Mono;

public class UserUseCase implements UserServicePort {

    private final UserRepositoryPort userRepository;
    private final CrediyaLoggerPort logger;

    public UserUseCase(
            UserRepositoryPort userRepository,
            CrediyaLoggerPort logger
    ) {
        this.userRepository = userRepository;
        this.logger = logger;
    }

    @Override
    public Mono<User> saveUser(User user) {
        logger.info("Saving user {}", user);

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
