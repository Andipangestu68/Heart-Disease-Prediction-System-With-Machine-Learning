package com.heart.disease.predict.config;


import com.heart.disease.predict.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService userService;

    // Bean untuk RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Bean untuk BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean untuk DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean untuk AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    // Konfigurasi Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Konfigurasi CSRF (dinonaktifkan untuk API)
                .csrf(csrf -> csrf.disable())
                // Konfigurasi Autorisasi
                .authorizeHttpRequests(authorize -> authorize
                        // Akses tanpa autentikasi untuk resource statis
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // Izinkan akses tanpa autentikasi ke endpoint tertentu
                        .requestMatchers("/api/heart/search", "/api/**").permitAll()
                        .requestMatchers("/api/v1/registration**", "/api/v1/login**").permitAll()
                        .requestMatchers("/api/v1/index").permitAll()  // Halaman index dapat diakses oleh siapa saja
                        .requestMatchers("/api/v1/forms").hasAnyRole("USER", "ADMIN") // Forms hanya untuk USER dan ADMIN
                        .requestMatchers("/api/v1/tutor").hasAnyRole("USER", "ADMIN") // Forms hanya untuk USER dan ADMIN
                        .requestMatchers("/api/v1/datasets").hasAnyRole("USER", "ADMIN") // Forms hanya untuk USER dan ADMIN
                        .requestMatchers("/api/v1/patients").hasRole("ADMIN")  // Patients hanya untuk ADMIN

                        // Semua permintaan lainnya memerlukan autentikasi
                        .anyRequest().authenticated()
                )

                // Konfigurasi Form Login
                .formLogin(form -> form
                        .loginPage("/api/v1/login")
                        .defaultSuccessUrl("/api/v1/index", true)
                        .permitAll()
                )

                // Konfigurasi Logout
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/logout"))
                        .logoutSuccessUrl("/api/v1/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
