package _c.shop.user.service;

import _c.shop.global.jwt.UserPrincipal;
import _c.shop.user.dto.UserResponseDto;

import java.util.Optional;

public interface UserQueryService {

    Optional<UserPrincipal> getUserPrincipalByEmail(String email);

    UserResponseDto.GetUserInfoDto getUserInfo(String accessToken);
}
