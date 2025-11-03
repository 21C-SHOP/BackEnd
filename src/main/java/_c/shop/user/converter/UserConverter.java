package _c.shop.user.converter;

import _c.shop.oauth.OauthServerType;
import _c.shop.user.domain.OauthId;
import _c.shop.user.domain.User;
import _c.shop.user.domain.UserRole;
import _c.shop.user.domain.UserStatus;
import _c.shop.user.dto.UserRequestDto;
import _c.shop.user.dto.UserResponseDto;
import _c.shop.user.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User toUser(UserRequestDto.SignUpDto signUpDto) {
        return User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .zipCode(signUpDto.getZipCode())
                .address1(signUpDto.getAddress1())
                .address2(signUpDto.getAddress2())
                .phoneNumber(signUpDto.getPhoneNumber())
                .birth(signUpDto.getBirth())
                .point(0D)
                .lastLoginAt(LocalDateTime.now())
                .isVerified(true)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .oauthId(new OauthId("", OauthServerType.GENERAL))
                .build();
    }

    public UserResponseDto.GetUserInfoDto toGetUserInfoDto(UserInfoVO userInfoVO) {
        return UserResponseDto.GetUserInfoDto.builder()
                .email(userInfoVO.getEmail())
                .name(userInfoVO.getName())
                .zipCode(userInfoVO.getZipCode())
                .address1(userInfoVO.getAddress1())
                .address2(userInfoVO.getAddress2())
                .phoneNumber(userInfoVO.getPhoneNumber())
                .birth(userInfoVO.getBirth())
                .build();
    }

    public UserResponseDto.VerifyEmailDto toVerifyEmailDto(Boolean isAvailable) {
        return UserResponseDto.VerifyEmailDto.builder()
                .isAvailable(isAvailable)
                .build();
    }
}
