package co.com.crediya.r2dbc.persistence.endpoint;

import co.com.crediya.r2dbc.entities.EndpointEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EndpointRepository extends ReactiveCrudRepository<EndpointEntity, Long>, ReactiveQueryByExampleExecutor<EndpointEntity> {


    @Query("""
            SELECT CASE WHEN EXISTS
                (
                    SELECT 1
                        FROM roles r
                        INNER JOIN role_endpoints re ON r.id = re.id_role
                        INNER JOIN endpoints e ON e.id = re.id_endpoint
                        WHERE r.code = :roleCode AND e.path = :path AND e.method = :method
                )
                THEN 'true' ELSE 'false' END
        """)
    Mono<Boolean> existsEndpointByRoleCodeAndPathAndMethod(String roleCode, String path, String method);

}
