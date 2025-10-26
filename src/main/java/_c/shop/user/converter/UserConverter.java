package _c.shop.user.converter;

import _c.shop.user.dto.UserResponseDto;
import _c.shop.user.vo.UserInfoVO;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

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
}
