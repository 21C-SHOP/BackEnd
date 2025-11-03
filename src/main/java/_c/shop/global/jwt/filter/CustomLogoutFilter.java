package _c.shop.global.jwt.filter;

import _c.shop.global.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private static final String LOGOUT_URI = "/v1/users/logout";
    private static final String LOGOUT_METHOD = "POST";

    private final JwtService jwtService;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        if (!isLogoutRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        if (refreshToken.isEmpty() || !isTokenValid(refreshToken.get()) || jwtService.isNotInRedis(refreshToken.get())) {
            log.info("로그아웃 처리 실패, 유효하지 않은 refreshToken : {}", refreshToken.orElse("없음"));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        deleteRefreshTokenInRedis(response, refreshToken.get());
    }

    private boolean isLogoutRequest(final HttpServletRequest request) {
        return request.getRequestURI().equals(LOGOUT_URI) && request.getMethod().equals(LOGOUT_METHOD);
    }

    private boolean isTokenValid(final String refreshToken) {
        try {
            jwtService.isTokenValid(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void deleteRefreshTokenInRedis(final HttpServletResponse response, final String refreshToken) {
        log.info("로그아웃 처리, refreshToken : {}", refreshToken);
        jwtService.deleteRefreshToken(refreshToken);

        Cookie cookie = new Cookie(jwtService.getRefreshHeader(), null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}