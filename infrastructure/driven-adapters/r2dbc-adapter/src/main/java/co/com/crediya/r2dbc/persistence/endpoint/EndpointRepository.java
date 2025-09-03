package co.com.crediya.r2dbc.persistence.endpoint;

import co.com.crediya.r2dbc.entities.EndpointEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EndpointRepository extends ReactiveCrudRepository<EndpointEntity, Long>, ReactiveQueryByExampleExecutor<EndpointEntity> {


    @Query("""
            SELECT e.*
            FROM roles r
            INNER JOIN role_endpoints re ON r.id = re.id_role
            INNER JOIN endpoints e ON e.id = re.id_endpoint
            WHERE r.code = :roleCode
        """)
    Flux<EndpointEntity> findByRoleCode(String roleCode);

}
