package _c.shop.jwt.filter;

import _c.shop.apiPayload.exception.TokenInvalidException;
import _c.shop.jwt.UserPrincipal;
import _c.shop.jwt.service.JwtService;
import _c.shop.jwt.util.PasswordUtil;
import _c.shop.user.service.UserQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


/**
 * Jwt 인증 필터 로그인 이외의 URI 요청이 왔을 때 처리하는 필터
 * AccessToken 유효성 검사를 진행하고 유효하지 않으면 프론트로 에러 메시지를 보낸다. 프론트에서 에러 메시지를 받으면 재발급 API CALL
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final Set<String> SWAGGER_PATH_PREFIXES = Set.of(
            "/swagger", "/v3/api-docs", "/api-docs", "/swagger-ui.html", "/swagger-ui",
            "/webjars", "/favicon.ico", "/csrf", "/v3/api-docs.yaml", "/v3/api-docs.json",
            "/swagger-resources", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security"
    );

    private static final Set<String> NOT_APPLY_JWT_FILTER_PREFIXES = Set.of(
            "/v1/users/sign-up", "/v1/users/verifications", "/v1/oauth", "/v1/reissue"
    );

    private final JwtService jwtService;
    private final UserQueryService userQueryService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        if (isSwaggerPath(request) || isNotApplyJwtPath(request)) {
            log.debug("JWT Authentication Filter Skip");
            filterChain.doFilter(request, response);
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    /**
     * [AccessToken 체크 & 인증 처리 메소드] request에서 extractAccessToken()으로 AccessToken 추출 후, isTokenValid()로 유효한 토큰인지 검증 유효한
     * 토큰이면, AccessToken서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환 그 유저 객체를
     * saveAuthentication()으로 인증 처리하여 인증 허가 처리된 객체를 SecurityContextHolder에 담기 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                  final FilterChain filterChain) throws IOException, ServletException {
        if (isSwaggerPath(request)) {
            log.info("Swagger 토큰 미필요");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        if (accessToken == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 없습니다");
            return;
        }

        try {
            jwtService.isTokenValid(accessToken);
            jwtService.extractEmail(accessToken)
                    .flatMap(userQueryService::getUserPrincipalByEmail)
                    .ifPresent(this::saveAuthentication);
        } catch (TokenInvalidException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        } catch (Exception e) {
            log.info(e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰 오류 : " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * UserDetails 객체 생성 -> Authentication 객체 생성 -> SecurityContextHolder에 담기
     */
    public void saveAuthentication(final UserPrincipal userPrincipal) {
        UserDetails userDetailsUser = this.getUserDetails(userPrincipal);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("JWT 토큰 검증 완료");
    }

    private boolean isNotApplyJwtPath(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        return NOT_APPLY_JWT_FILTER_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private boolean isSwaggerPath(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        return SWAGGER_PATH_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private UserDetails getUserDetails(final UserPrincipal userPrincipal) {
        String password = PasswordUtil.generateRandomPassword();
        return org.springframework.security.core.userdetails.User.builder()
                .username(userPrincipal.getEmail())
                .password(password)
                .roles(userPrincipal.getRole().name())
                .build();
    }

    // 오류 정보 {status, message}를 JSON 형태로 바꿔서 응답
    // 전역 예외 처리는 필터에서 동작하지 않으므로, 필터 내에서 직접 에러 응답을 작성
    private void sendErrorResponse(final HttpServletResponse response, final int statusCode, final String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
        mapper.writeValue(out, errorResponse);
    }

    @Getter
    @Setter
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
