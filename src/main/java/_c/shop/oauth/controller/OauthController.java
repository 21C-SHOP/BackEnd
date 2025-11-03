package _c.shop.oauth.controller;

import _c.shop.global.apiPayload.ApiResponse;
import _c.shop.global.apiPayload.code.status.SuccessStatus;
import _c.shop.oauth.OauthServerType;
import _c.shop.oauth.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")

/**
 * @Pathvariable을 통해 /oauth/kakao 등의 요청에서 kakao 부분을 oauthServerType으로 변환한다 (converter 이용)
 * 사용자가 프론트에서 /oauth/kakao를 통해 접속하면 밑 controller를 통한다.
 * oauthService를 통해 AuthCode를 받아오기 위한 URL을 생성하고 이 URL로 사용자를 Redirect시킨다.
 */

public class OauthController {

    private final OauthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<ApiResponse<Void>> redirectAuthCodeRequestUrl(
            @PathVariable("oauthServerType") final OauthServerType oauthServerType,
            final HttpServletResponse response
    ) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok(ApiResponse.ofNoting());
    }

    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<ApiResponse<Boolean>> login(
            @PathVariable("oauthServerType") final OauthServerType oauthServerType,
            @RequestParam("code") final String code,
            final HttpServletResponse response
    ) {
        boolean isNotNewUser = oauthService.login(response, oauthServerType, code);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, isNotNewUser));
    }
}
