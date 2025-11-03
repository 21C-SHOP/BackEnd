package _c.shop.user.service;

import _c.shop.global.jwt.UserPrincipal;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface UserQueryService {

    void login(HttpServletResponse response, UserRequestDto.LoginDto loginDto);

    Optional<UserPrincipal> getUserPrincipalByEmail(String email);

    UserResponseDto.GetUserInfoDto getUserInfo(String accessToken);
}
