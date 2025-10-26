package _c.shop.user.controller;

import _c.shop.apiPayload.ApiResponse;
import _c.shop.user.service.UserCommandService;
import _c.shop.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=UTF-8")
public class UserController {

    private UserCommandService userCommandService;
    private UserQueryService userQueryService;

    @PostMapping("/v1/users/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp() {

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @GetMapping("/v1/users/info")
    public void getUserInfo() {

    }

    @PatchMapping("/v1/users/info")
    public void updateUserInfo() {

    }

    @PostMapping("/v1/users/verifications")
    public void sendVerificationEmail() {

    }

    @PostMapping("/v1/users/verifications/confirm")
    public void confirmVerificationCode() {

    }

    @GetMapping("/v1/users/mypage")
    public void getMyPage() {

    }
}
