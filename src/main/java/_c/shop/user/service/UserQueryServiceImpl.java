package _c.shop.user.service;

import _c.shop.global.apiPayload.code.status.ErrorStatus;
import _c.shop.global.apiPayload.exception.ExceptionHandler;
import _c.shop.global.jwt.UserPrincipal;
import _c.shop.global.jwt.service.JwtService;
import _c.shop.user.converter.UserConverter;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.dto.UserResponseDto;
import _c.shop.user.repository.UserRepository;
import _c.shop.user.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public void login(HttpServletResponse response, UserRequestDto.LoginDto loginDto) {
        String email = loginDto.getEmail();
        boolean isExists = userRepository.existsByEmail(email);
        if (!isExists) {
            throw new ExceptionHandler(ErrorStatus._USER_NOT_FOUND);
        }
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(email, refreshToken);
        log.info("사용자 로그인, email : {}", email);
    }

    @Override
    public Optional<UserPrincipal> getUserPrincipalByEmail(String email) {
        return userRepository.findRoleByEmail(email)
                .map(role -> UserPrincipal.builder()
                        .email(email)
                        .role(role)
                        .build());
    }

    @Override
    public UserResponseDto.GetUserInfoDto getUserInfo(String accessToken) {
        String email = jwtService.extractEmail(accessToken).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._ACCESSTOKEN_NOT_FOUND));
        UserInfoVO vo = userRepository.findUserInfoVOByEmail(email).orElseThrow(() ->
                new ExceptionHandler(ErrorStatus._USER_NOT_FOUND));
        return userConverter.toGetUserInfoDto(vo);
    }
}
