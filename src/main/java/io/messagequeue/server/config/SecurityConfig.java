package io.messagequeue.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.messagequeue.server.service.auth.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final String[] WHITE_LIST = {"/api/v1/register", "/login", "/register", "/css/**", "/js/**", "/images/**"};
        private final String ADMIN_REQUEST_MATCHER = "/admin/**";
        private final String USER_REQUEST_MATCHER = "/user/**";

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(WHITE_LIST).permitAll()
                                        .requestMatchers(ADMIN_REQUEST_MATCHER).hasRole("ADMIN") // Chỉ ADMIN
                                        .requestMatchers(USER_REQUEST_MATCHER).hasAnyRole("CUSTOMER", "ADMIN")
                                        .anyRequest().authenticated())
                        .formLogin(login -> login
                                        .loginPage("/login")
                                        .loginProcessingUrl("/process-login")
                                        .defaultSuccessUrl("/web/products-ajax", true)
                                        .permitAll())
                        .logout(logout -> logout
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/login?logout")
                                        .invalidateHttpSession(true)
                                        .deleteCookies("JSESSIONID"))
                        .authenticationProvider(authenticationProvider())
                        .build();
        }

        @SuppressWarnings("deprecation")
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(customUserDetailsService); // Inject bean CustomUserDetailsService
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
