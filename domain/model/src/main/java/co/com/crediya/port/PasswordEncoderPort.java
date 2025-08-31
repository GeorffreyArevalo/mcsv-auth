package co.com.crediya.port;

public interface PasswordEncoderPort {

    Boolean verify(String password, String encodePassword);

    String encode(String password);

}
