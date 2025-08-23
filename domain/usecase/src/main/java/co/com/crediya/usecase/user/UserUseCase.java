package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepositoryPort;
import reactor.core.publisher.Mono;

public class UserUseCase implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Void> saveUser(User user) {

        return Mono.just(user)
                .flatMap(UserValidator::validateSaveUser)
                .flatMap(userRepository::save)
                .then();
    }
}
