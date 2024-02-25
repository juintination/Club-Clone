package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;

@Configuration
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/sample/all").permitAll()
                .requestMatchers("/sample/member").hasRole("USER")
                .anyRequest().authenticated()
        );
        http.formLogin(config -> {});
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout(config -> {});
        http.oauth2Login(config -> {
            config.successHandler(successHandler());
        });
        http.rememberMe(config -> {
            config.tokenValiditySeconds(60 * 60 * 24 * 7); // 7days
        });
        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
