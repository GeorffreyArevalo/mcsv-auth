package co.com.crediya.security.enums;

import lombok.Getter;

@Getter
public enum SecurityConstants {

    TYPE_ALGORITHM("RSA"),
    REGEX_START_END_PUBLIC_KEY("-----\\w+ PUBLIC KEY-----"),
    REGEX_START_END_PRIVATE_KEY("-----\\w+ PRIVATE KEY-----"),
    TYPE_TOKEN("Bearer"),
    REGEX_TYPE_TOKEN("Bearer "),
    ROLE_CLAIM("role"),
    SCOPE_CLAIM("scope"),
    PREFIX_ROLE_AUTH("ROLE_"),
    TOKEN_CLAIM("token"),
    REGEX_SPACES("\\s");

    private final String value;
    SecurityConstants(String value) {
        this.value = value;
    }

}
