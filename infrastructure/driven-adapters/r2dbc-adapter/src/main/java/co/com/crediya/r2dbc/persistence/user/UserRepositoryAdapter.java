package co.com.crediya.r2dbc.persistence.user;

import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long,  UserRepository> implements UserRepositoryPort {

    private final TransactionalOperator transactionalOperator;

    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> saveUser(User user){
        return transactionalOperator.execute(
                tr -> save(user)
        ).single();
    }

    @Override
    public Mono<Boolean> existByEmailAndDocument(String email, String document) {
        return repository.existsByEmailAndDocument(email, document);
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return repository.findByDocument(document).map(super::toEntity);
    }


}
