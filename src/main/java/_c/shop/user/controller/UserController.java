package _c.shop.user.controller;

import _c.shop.apiPayload.ApiResponse;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.dto.UserResponseDto;
import _c.shop.user.service.UserCommandService;
import _c.shop.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static _c.shop.jwt.JwtConstants.ACCESS_TOKEN_PREFIX;
import static _c.shop.jwt.JwtConstants.ACCESS_TOKEN_REPLACEMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/v1/users/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp() {

        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @GetMapping("/v1/users/info")
    public ResponseEntity<ApiResponse<UserResponseDto.GetUserInfoDto>> getUserInfo(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.replace(ACCESS_TOKEN_PREFIX, ACCESS_TOKEN_REPLACEMENT);
        UserResponseDto.GetUserInfoDto userInfoDto = userQueryService.getUserInfo(accessToken);
        return ResponseEntity.ok(ApiResponse.onSuccess(userInfoDto));
    }

    @PostMapping("/v1/users/info")
    public ResponseEntity<ApiResponse<Void>> createUserInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserRequestDto.InitUserInfoDto userInfoDto) {
        String accessToken = authorizationHeader.replace(ACCESS_TOKEN_PREFIX, ACCESS_TOKEN_REPLACEMENT);
        userCommandService.createUserInfo(accessToken, userInfoDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @PatchMapping("/v1/users/info")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserRequestDto.UpdateUserInfoDto updateUserInfoDto) {
        String accessToken = authorizationHeader.replace(ACCESS_TOKEN_PREFIX, ACCESS_TOKEN_REPLACEMENT);
        userCommandService.updateUserInfo(accessToken, updateUserInfoDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
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
