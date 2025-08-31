package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class KeysUtil {

    @Value("${security.jwt.private-key-location}")
    private Resource resourcePrivateKey;

    @Value("${security.jwt.public-key-location}")
    private Resource resourcePublicKey;

    public Mono<PrivateKey> loadPrivateKey() {
        return  Mono.fromCallable( () -> {
                    String key = new String( resourcePrivateKey.getInputStream().readAllBytes() )
                        .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
                    return KeyFactory.getInstance("RSA").generatePrivate(spec);
                })
                .onErrorResume( error -> Mono.error(new CrediyaUnathorizedException(error.getMessage())) );
    }

    public Mono<PublicKey> loadPublicKey() {
        return  Mono.fromCallable( () -> {
                    String key = new String( resourcePublicKey.getInputStream().readAllBytes() )
                            .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                            .replaceAll("\\s", "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                    return KeyFactory.getInstance("RSA").generatePublic(spec);
                })
                .onErrorResume( error -> Mono.error(new CrediyaUnathorizedException(error.getMessage())) );
    }


}
