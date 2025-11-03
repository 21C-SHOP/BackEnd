package _c.shop.user.controller;

import _c.shop.global.apiPayload.ApiResponse;
import _c.shop.global.event.service.EmailEventPublisher;
import _c.shop.global.redis.EmailCodeRedisService;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.dto.UserResponseDto;
import _c.shop.user.service.UserCommandService;
import _c.shop.user.service.UserQueryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static _c.shop.global.jwt.JwtConstants.ACCESS_TOKEN_PREFIX;
import static _c.shop.global.jwt.JwtConstants.ACCESS_TOKEN_REPLACEMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final EmailCodeRedisService emailCodeRedisService;
    private final EmailEventPublisher emailEventPublisher;

    @PostMapping("/v1/users/login")
    public ResponseEntity<ApiResponse<Void>> logIn(
            HttpServletResponse response,
            @RequestBody UserRequestDto.LoginDto loginDto
    ) {
        userQueryService.login(response, loginDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @PostMapping("/v1/users/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp(
            @RequestBody UserRequestDto.SignUpDto signUpDto
    ) {
        userCommandService.signup(signUpDto);
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

    /**
     * 이메일 코드 발송 API
     * MQ로 이벤트 발행
     */
    @PostMapping("/v1/users/verifications")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(
            @RequestBody UserRequestDto.SendEmailVerificationDto sendEmailVerificationDto
    ) {
        emailEventPublisher.publishEmailVerificationEvent(sendEmailVerificationDto.getEmail());
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    /**
     * 이메일 코드 검증 API
     * Redis에서 코드 검증
     */
    @PostMapping("/v1/users/verifications/confirm")
    public ResponseEntity<ApiResponse<UserResponseDto.VerifyEmailDto>> confirmVerificationCode(
            @RequestBody UserRequestDto.VerifyEmailDto verifyEmailDto
    ) {
        UserResponseDto.VerifyEmailDto dto = emailCodeRedisService.verifyEmailCode(verifyEmailDto.getEmail(), verifyEmailDto.getVerificationCode());
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @GetMapping("/v1/users/mypage")
    public void getMyPage() {

    }
}
