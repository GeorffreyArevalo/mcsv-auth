package co.com.crediya.r2dbc.persistence.role;


import co.com.crediya.model.Role;
import co.com.crediya.model.gateways.RoleRepositoryPort;
import co.com.crediya.r2dbc.entities.RoleEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<Role, RoleEntity, Long, RoleRepository> implements RoleRepositoryPort {

    public RoleRepositoryAdapter(RoleRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Mono<Role> findByCode(String code) {
        return repository.findByCode(code).map(super::toEntity);
    }
}
