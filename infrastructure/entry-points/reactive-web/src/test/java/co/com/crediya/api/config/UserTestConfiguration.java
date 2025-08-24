package co.com.crediya.api.config;

import co.com.crediya.api.mappers.UserMapper;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@TestConfiguration
public class UserTestConfiguration {

    @Bean
    public UserMapper userMapper() {
         return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "mysql")
                        .option(HOST, "localhost")
                        .option(PORT, 3306)
                        .option(USER, "user")
                        .option(PASSWORD, "1234")
                        .option(DATABASE, "auth")
                        .build()
        );
    }

    @Bean
    public R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(R2dbcTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

}
