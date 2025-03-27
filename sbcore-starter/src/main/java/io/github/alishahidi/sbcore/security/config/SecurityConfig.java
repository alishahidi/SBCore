package io.github.alishahidi.sbcore.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alishahidi.sbcore.i18n.I18nUtil;
import io.github.alishahidi.sbcore.response.ApiResponse;
import io.github.alishahidi.sbcore.security.jwt.JwtAuthenticationFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Locale;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    JwtAuthenticationFilter jwtAuthFilter;
    AuthenticationProvider authenticationProvider;
    I18nUtil i18nUtil;
    ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/hello/**",
                                "/error",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/v2/api-docs/**",
                                "/swagger-resources/**",
                                "/api/document/stream/**",
                                "/api/home",
                                "/api/home/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            Locale locale = LocaleContextHolder.getLocale();
                            ApiResponse<String> apiResponse = ApiResponse.error(
                                    i18nUtil.getMessage("access.denied", locale),
                                    HttpStatus.FORBIDDEN
                            );

                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            objectMapper.writeValue(response.getWriter(), apiResponse);
                        })
                        .authenticationEntryPoint((request, response, authenticationException) -> {
                            Locale locale = LocaleContextHolder.getLocale();
                            ApiResponse<String> apiResponse =
                                    ApiResponse.error(i18nUtil.getMessage("authentication.failed", locale), HttpStatus.UNAUTHORIZED);

                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            objectMapper.writeValue(response.getWriter(), apiResponse);
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
        ;
        return http.build();
    }
}