package co.com.crediya.security.config;

import co.com.crediya.security.jwt.JwtFilter;
import co.com.crediya.security.repository.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final SecurityContextRepository securityContextRepository;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {

            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange( specExchange ->
                        specExchange.pathMatchers( HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                .pathMatchers( HttpMethod.POST, "/api/v1/users").hasAnyRole("ADMIN", "ADVISER")
                                .anyExchange().authenticated()
                    )
                    .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
                    .securityContextRepository(securityContextRepository)
                    .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                    .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                    .logout(ServerHttpSecurity.LogoutSpec::disable)
                    .build();

        }


}
