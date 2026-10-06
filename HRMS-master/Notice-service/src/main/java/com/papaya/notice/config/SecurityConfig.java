package com.papaya.notice.config;



import com.papaya.notice.util.JwtAuthenticationFilter;
import com.papaya.notice.util.PermissionAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private PermissionAuthorizationFilter permissionAuthorizationFilter;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers(HttpMethod.GET,"/api/notice/routes").permitAll();
                    auth.requestMatchers(
                            "/v3/api-docs/**",      // OpenAPI docs
                            "/swagger-ui.html",     // Swagger UI HTML
                            "/swagger-ui/**",       // Swagger UI resources
                            "/webjars/**"           // Webjars for swagger UI static resources
                    ).permitAll();

                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(permissionAuthorizationFilter, JwtAuthenticationFilter.class);


        return http.build();
    }
}
