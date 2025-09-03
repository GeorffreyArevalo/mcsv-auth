package co.com.crediya.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.mockito.Mockito.when;

class KeysUtilTest {

    @Mock
    private Resource privateKeyResource;

    @Mock
    private Resource publicKeyResource;

    @InjectMocks
    private KeysUtil keysUtil;

    private String privateKeyPem;
    private String publicKeyPem;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(privateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----";

        publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";

        when(privateKeyResource.getInputStream())
                .thenReturn(new ByteArrayInputStream(privateKeyPem.getBytes()));

        when(publicKeyResource.getInputStream())
                .thenReturn(new ByteArrayInputStream(publicKeyPem.getBytes()));

        keysUtil = new KeysUtil(privateKeyResource, publicKeyResource);
    }

    @Test
    void shouldLoadPrivateKeySuccessfully() {
        StepVerifier.create(keysUtil.loadPrivateKey())
                .expectNextMatches(key -> key.getAlgorithm().equals("RSA"))
                .verifyComplete();
    }

    @Test
    void shouldLoadPublicKeySuccessfully() {
        StepVerifier.create(keysUtil.loadPublicKey())
                .assertNext(key -> key.getAlgorithm().equals("RSA"))
                .verifyComplete();
    }

}

