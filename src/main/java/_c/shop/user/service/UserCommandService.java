package _c.shop.user.service;

import _c.shop.user.dto.UserRequestDto;

public interface UserCommandService {

    void createUserInfo(String accessToken, UserRequestDto.InitUserInfoDto userInfoDto);

    void updateUserInfo(String accessToken, UserRequestDto.UpdateUserInfoDto updateUserInfoDto);
}
