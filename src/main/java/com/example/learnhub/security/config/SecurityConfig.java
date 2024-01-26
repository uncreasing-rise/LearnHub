package com.example.learnhub.security.config;


import com.example.learnhub.security.CustomAuthorizationFilter;
import com.example.learnhub.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true)
//@RequiredArgsConstructor

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated()
                    .requestMatchers(
                        "/api/user/v1/resetPassword"
                    ).hasRole(Role.ADMIN.name())
                    .anyRequest().permitAll()
            )
            .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
