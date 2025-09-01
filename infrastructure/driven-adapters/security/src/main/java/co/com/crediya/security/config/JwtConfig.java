package co.com.crediya.security.config;

import co.com.crediya.security.jwt.JwtProvider;
import co.com.crediya.security.util.KeysUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.expiration}")
    private Integer expiration;

    @Value("${security.jwt.private-key-location}")
    private Resource resourcePrivateKey;

    @Value("${security.jwt.public-key-location}")
    private Resource resourcePublicKey;

    @Bean
    public JwtProvider jwtProvider(KeysUtil keysUtil) {
        return new JwtProvider(keysUtil, expiration);
    }

    @Bean
    public KeysUtil keysUtil() {
        return new KeysUtil(resourcePrivateKey, resourcePublicKey);
    }


}
