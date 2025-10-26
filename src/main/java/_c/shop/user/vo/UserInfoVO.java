package _c.shop.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private String email;
    private String name;
    private String zipCode;
    private String address1;
    private String address2;
    private String phoneNumber;
    private LocalDate birth;
}
