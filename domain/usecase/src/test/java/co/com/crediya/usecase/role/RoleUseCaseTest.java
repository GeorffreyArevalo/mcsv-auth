package co.com.crediya.usecase.role;

import co.com.crediya.exceptions.CrediyaResourceNotFoundException;
import co.com.crediya.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.Role;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RoleUseCaseTest {

    private RoleRepositoryPort roleRepositoryPort;
    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        roleRepositoryPort = Mockito.mock(RoleRepositoryPort.class);
        roleUseCase = new RoleUseCase(roleRepositoryPort);
    }

    @Test
    @DisplayName("Should return role when exists")
    void shouldReturnRoleWhenExists() {
        String code = "ADMIN";
        Role role = new Role();
        role.setCode(code);

        when(roleRepositoryPort.findByCode(code)).thenReturn(Mono.just(role));


        StepVerifier.create(roleUseCase.findByCode(code))
                .expectNextMatches(foundRole -> foundRole.getCode().equals(code))
                .verifyComplete();

        verify(roleRepositoryPort, times(1)).findByCode(code);
    }

    @Test
    @DisplayName("Should throw error when role does not exists")
    void shouldThrowExceptionWhenRoleNotExists() {
        String code = "INVALID";
        when(roleRepositoryPort.findByCode(code)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.findByCode(code))
                .expectErrorMatches(throwable ->
                        throwable instanceof CrediyaResourceNotFoundException &&
                                throwable.getMessage().equals(
                                        String.format(ExceptionMessages.ROLE_WITH_CODE_NOT_EXISTS.getMessage(), code)
                                )
                )
                .verify();

        verify(roleRepositoryPort, times(1)).findByCode(code);
    }
}
