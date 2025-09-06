package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import co.com.crediya.security.enums.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeysUtilTest {

    private String validPrivateKey;
    private String validPublicKey;
    private String invalidPrivateKey;
    private String invalidPublicKey;

    @BeforeEach
    void setUp() throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        validPrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
                privateKeyBase64 +
                "\n-----END PRIVATE KEY-----";

        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        validPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                publicKeyBase64 +
                "\n-----END PUBLIC KEY-----";

        invalidPrivateKey = "-----BEGIN PRIVATE KEY-----\nINVALID_KEY\n-----END PRIVATE KEY-----";
        invalidPublicKey = "-----BEGIN PUBLIC KEY-----\nINVALID_KEY\n-----END PUBLIC KEY-----";
    }

    @Test
    @DisplayName("should load private key")
    void loadPrivateKeyShouldReturnValidKey() {
        KeysUtil keysUtil = new KeysUtil(validPrivateKey, validPublicKey);

        StepVerifier.create(keysUtil.loadPrivateKey())
                .assertNext(privateKey ->
                        assertEquals(SecurityConstants.TYPE_ALGORITHM.getValue(), privateKey.getAlgorithm())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("should throw exception if private key is invalid")
    void loadPrivateKeyShouldThrowExceptionWhenInvalid() {
        KeysUtil keysUtil = new KeysUtil(invalidPrivateKey, validPublicKey);

        StepVerifier.create(keysUtil.loadPrivateKey())
                .expectErrorMatches(CrediyaInternalServerErrorException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("should load public key")
    void loadPublicKeyShouldReturnValidKey() {
        KeysUtil keysUtil = new KeysUtil(validPrivateKey, validPublicKey);

        StepVerifier.create(keysUtil.loadPublicKey())
                .assertNext(publicKey ->
                        assertEquals(SecurityConstants.TYPE_ALGORITHM.getValue(), publicKey.getAlgorithm())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("should throw exception if public key is invalid")
    void loadPublicKeyShouldThrowExceptionWhenInvalid() {
        KeysUtil keysUtil = new KeysUtil(validPrivateKey, invalidPublicKey);

        StepVerifier.create(keysUtil.loadPublicKey())
                .expectErrorMatches(CrediyaInternalServerErrorException.class::isInstance)
                .verify();
    }
}
