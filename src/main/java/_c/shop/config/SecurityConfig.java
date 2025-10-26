package _c.shop.config;

import _c.shop.jwt.filter.CustomLogoutFilter;
import _c.shop.jwt.filter.JwtAuthenticationProcessingFilter;
import _c.shop.jwt.service.JwtService;
import _c.shop.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리 JwtAuthenticationProcessingFilter는
 * AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserQueryService userQueryService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                        corsConfigurationSource())) // cors 해제
                .formLogin(config -> config.disable()) // FormLogin 사용 X
                .logout(config -> config.disable())    // logout 사용 X
                .httpBasic(config -> config.disable()) // httpBasic 사용 X
                .csrf(config -> config.disable()) // csrf 보안 사용 X
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger/**", "/swagger-ui/index.html#/**", "/swagger-ui/**",
                                        "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico").permitAll()
                                .requestMatchers("/v1/reissue").permitAll() // refreshToken 재발급 가능
                                .requestMatchers("/v1/oauth/**").permitAll() // OAuth 경로 접근 가능
                                .requestMatchers("/v1/users/sign-up").permitAll() // 회원가입
                                .requestMatchers("/v1/users/verifications/**").permitAll() // 이메일 인증
                                .requestMatchers(("/v1/reissue")).permitAll() // refreshToken 재발급
                                .requestMatchers("/health").permitAll() // aws health check
                                .anyRequest().authenticated() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
                );
        http.addFilterBefore(customLogoutFilter(), LogoutFilter.class);
        http.addFilterAfter(jwtAuthenticationProcessingFilter(), CustomLogoutFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 도메인 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더 설정
        configuration.setAllowCredentials(true); // 자격 증명 허용 여부 설정
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Content-Type");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userQueryService);
        return jwtAuthenticationFilter;
    }

    @Bean
    public CustomLogoutFilter customLogoutFilter() {
        CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(jwtService);
        return customLogoutFilter;
    }
}
