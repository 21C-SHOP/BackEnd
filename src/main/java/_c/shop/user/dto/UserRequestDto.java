package _c.shop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class UserRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InitUserInfoDto {
        private String name;
        private String zipCode;
        private String address1;
        private String address2;
        private String phoneNumber;
        private LocalDate birth;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserInfoDto {
        private String name;
        private String zipCode;
        private String address1;
        private String address2;
        private String phoneNumber;
        private LocalDate birth;
    }
}
