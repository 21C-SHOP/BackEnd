package _c.shop.jwt.controller;

import _c.shop.apiPayload.ApiResponse;
import _c.shop.jwt.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @GetMapping("/v1/reissue")
    public ResponseEntity<ApiResponse<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = reissueService.getRefreshTokenInCookie(request);
        reissueService.reissueToken(response, refreshToken);
        return ResponseEntity.ok(ApiResponse.ofNoting());
    }
}
