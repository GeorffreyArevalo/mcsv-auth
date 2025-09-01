package co.com.crediya.model.gateways;

import co.com.crediya.model.Role;
import reactor.core.publisher.Mono;

public interface RoleRepositoryPort {

    Mono<Role> findById(Long id);
    Mono<Role> findByCode(String code);

}
