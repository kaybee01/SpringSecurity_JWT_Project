package org.scaler.securityjwt.Config;

import org.scaler.securityjwt.Filter.JWTAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.fasterxml.jackson.annotation.PropertyAccessor.CREATOR;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JWTAuthFilter jwtAuthFilter;

    SecurityConfig(JWTAuthFilter jwtAuthFilter)
    {
        this.jwtAuthFilter=jwtAuthFilter;

    }

    private static final String[] publicRoutes = {
            "/error", "/auth/**", "/home.html"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> {
                            try {
                                requests
                                        .anyRequest().permitAll()
                                        .and().cors().disable()
                                        .csrf().disable()
                                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );


//                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


