package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaUnathorizedException;
import co.com.crediya.security.enums.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class KeysUtil {

    private final Resource resourcePrivateKey;

    private final Resource resourcePublicKey;

    public Mono<PrivateKey> loadPrivateKey() {
        return  Mono.fromCallable( () -> {
                    String key = new String( resourcePrivateKey.getInputStream().readAllBytes() )
                        .replaceAll(SecurityConstants.REGEX_START_END_PRIVATE_KEY.getValue(), "")
                        .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
                    return KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue()).generatePrivate(spec);
                })
                .onErrorResume( error -> Mono.error(new CrediyaUnathorizedException(error.getMessage())) );
    }

    public Mono<PublicKey> loadPublicKey() {
        return  Mono.fromCallable( () -> {
                    String key = new String( resourcePublicKey.getInputStream().readAllBytes() )
                            .replaceAll(SecurityConstants.REGEX_START_END_PUBLIC_KEY.getValue(), "")
                            .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

                    byte[] decoded = Base64.getDecoder().decode(key);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                    return KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue()).generatePublic(spec);
                })
                .onErrorResume( error -> Mono.error(new CrediyaUnathorizedException(error.getMessage())) );
    }


}
