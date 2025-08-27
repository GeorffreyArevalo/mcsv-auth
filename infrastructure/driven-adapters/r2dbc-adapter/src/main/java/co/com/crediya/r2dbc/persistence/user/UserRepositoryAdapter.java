package co.com.crediya.r2dbc.persistence.user;

import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserRepositoryPort;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long,  UserRepository> implements UserRepositoryPort {

    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user){
        return super.save(user);

    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(super::toEntity);
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return repository.findByDocument(document).map(super::toEntity);
    }


}
