package co.com.crediya.usecase.user;

import co.com.crediya.model.User;
import co.com.crediya.enums.ExceptionMessages;
import co.com.crediya.exceptions.CrediyaBadRequestException;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.ports.CrediyaLoggerPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {

    private final UserRepositoryPort userRepository;
    private final CrediyaLoggerPort logger;

    @Override
    public Mono<User> saveUser(User user) {

        logger.info("Saving user {}", user);

        return userRepository.findByEmail(user.getEmail())
                .flatMap( existingUser -> {

                            logger.warn("User with email={} already exists", user.getEmail());

                            return Mono.error( new CrediyaBadRequestException(
                                    String.format(ExceptionMessages.USER_WITH_EMAIL_EXIST.getMessage(),  user.getEmail())
                            ));
                        }
                ).switchIfEmpty(
                        userRepository.findByDocument(user.getDocument())
                                .flatMap( existingUser -> {

                                        logger.warn("User with document={} already exists", user.getDocument());

                                        return Mono.error( new CrediyaBadRequestException(
                                                String.format(ExceptionMessages.USER_WITH_DOCUMENT_EXIST.getMessage(),  existingUser.getDocument())
                                        ));
                                    }
                                )
                )
                .switchIfEmpty(
                        UserValidator.validateSaveUser(user)
                )
                .flatMap(
                        validUser -> userRepository.save(user)
                                .doOnSuccess(
                                        loggedUser -> logger.info("User saved successfully.")
                                )
                );

    }
}
