package co.com.crediya.security.adapters;

import co.com.crediya.port.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Boolean verify(String password, String encodePassword) {
        return passwordEncoder.matches(password, encodePassword);
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
