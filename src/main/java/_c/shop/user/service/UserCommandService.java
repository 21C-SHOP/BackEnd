package _c.shop.user.service;

import _c.shop.user.dto.UserRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserCommandService {

    void signup(UserRequestDto.SignUpDto signupDto);

    void createUserInfo(String accessToken, UserRequestDto.InitUserInfoDto userInfoDto);

    void updateUserInfo(String accessToken, UserRequestDto.UpdateUserInfoDto updateUserInfoDto);
}
