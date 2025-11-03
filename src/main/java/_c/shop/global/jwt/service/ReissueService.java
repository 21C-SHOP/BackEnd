package _c.shop.global.jwt.service;

import _c.shop.global.apiPayload.code.status.ErrorStatus;
import _c.shop.global.apiPayload.exception.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReissueService {

    private final JwtService jwtService;

    public String getRefreshTokenInCookie(HttpServletRequest request) {
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        if (refreshToken.isPresent()) {
            validateRefreshToken(refreshToken.get());
            validateRefreshTokenIsBlackList(refreshToken.get());
            return refreshToken.get();
        }
        throw new ExceptionHandler(ErrorStatus._REFRESHTOKEN_NOT_FOUND);
    }

    public void reissueToken(HttpServletResponse response, String refreshToken) {
        String email = jwtService.getEmailFromRefreshToken(refreshToken);
        String newAccessToken = jwtService.createAccessToken(email);
        String newRefreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);
        jwtService.deleteRefreshToken(refreshToken);
        jwtService.updateRefreshToken(email, newRefreshToken);

        log.info("사용자 토큰 재발급, email : {}", email);
    }

    private void validateRefreshToken(final String refreshToken) {
        try {
            jwtService.isTokenValid(refreshToken);
        } catch (Exception e) {
            throw new ExceptionHandler(ErrorStatus._REFRESHTOKEN_NOT_VALID);
        }
    }

    private void validateRefreshTokenIsBlackList(final String refreshToken) {
        if (jwtService.isNotInRedis(refreshToken)) {
            throw new ExceptionHandler(ErrorStatus._REFRESHTOKEN_NOT_FOUND);
        }
    }
}
