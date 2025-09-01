package co.com.crediya.usecase.role;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Role;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Mono<Role> findByCode(String code) {
        return roleRepositoryPort.findByCode(code)
                .switchIfEmpty(Mono.error( new CrediyaResourceNotFoundException(String.format(
                        ExceptionMessages.ROLE_WITH_CODE_NOT_EXISTS.getMessage(), code
                ))));
    }

}
