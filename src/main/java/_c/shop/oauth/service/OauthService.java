package _c.shop.oauth.service;

import _c.shop.global.jwt.service.JwtService;
import _c.shop.oauth.AuthCodeRequestUrlProviderComposite;
import _c.shop.oauth.OauthServerType;
import _c.shop.oauth.client.OauthUserClientComposite;
import _c.shop.user.domain.User;
import _c.shop.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
/**
 * OauthServerType을 받아서 해당 인증 서버에서 Auth Code를 받아오기 위한 URL 주소 생성
 * 로그인
 */
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthUserClientComposite oauthUserClientComposite;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String getAuthCodeRequestUrl(final OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public boolean login(final HttpServletResponse response,
                         final OauthServerType oauthServerType,
                         final String authCode) {
        User user = oauthUserClientComposite.fetch(oauthServerType, authCode); // OAuth 서버에서 사용자 정보를 받아와서 User 객체 생성
        boolean isNotNewUser = userRepository.existsByOauthId(user.getOauthId());
        if (!isNotNewUser)
            userRepository.save(user);

        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        log.info("사용자 로그인 완료, email : {}", user.getEmail());
        return isNotNewUser;
    }
}
