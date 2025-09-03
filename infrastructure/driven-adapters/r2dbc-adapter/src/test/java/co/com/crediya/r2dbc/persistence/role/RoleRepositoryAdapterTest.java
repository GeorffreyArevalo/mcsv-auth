package co.com.crediya.r2dbc.persistence.role;

import co.com.crediya.model.Role;
import co.com.crediya.r2dbc.entities.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @InjectMocks
    RoleRepositoryAdapter roleRepositoryAdapter;

    @Mock
    RoleRepository repository;

    @Mock
    ObjectMapper mapper;

    private Role role;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .code("ADMIN")
                .name("Administrator")
                .build();

        roleEntity = RoleEntity.builder()
                .id(1L)
                .code("ADMIN")
                .name("Administrator")
                .build();
    }

    @Test
    @DisplayName("Must find role by code")
    void mustFindRoleByCode() {

        when(repository.findByCode(roleEntity.getCode())).thenReturn(Mono.just(roleEntity));
        when(mapper.map(roleEntity, Role.class)).thenReturn(role);


        Mono<Role> result = roleRepositoryAdapter.findByCode(roleEntity.getCode());

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getCode().equals(role.getCode()) && r.getName().equals(role.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return empty when role code does not exist")
    void mustReturnEmptyWhenRoleCodeNotExist() {
        String invalidCode = "INVALID";
        when(repository.findByCode(invalidCode)).thenReturn(Mono.empty());

        Mono<Role> result = roleRepositoryAdapter.findByCode(invalidCode);

        StepVerifier.create(result)
                .verifyComplete();
    }
}

