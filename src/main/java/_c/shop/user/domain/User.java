package _c.shop.user.domain;

import _c.shop.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String zipCode;
    private String address1;
    private String address2;
    private String phoneNumber;
    private LocalDate birth;
    private Double point;
    private LocalDateTime lastLoginAt;
    private Boolean isVerified;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Embedded
    private OauthId oauthId;

    public void updateUserInfo(UserRequestDto.InitUserInfoDto userInfo) {
        this.name = userInfo.getName();
        this.zipCode = userInfo.getZipCode();
        this.address1 = userInfo.getAddress1();
        this.address2 = userInfo.getAddress2();
        this.phoneNumber = userInfo.getPhoneNumber();
        this.birth = userInfo.getBirth();
    }

    public void updateUserInfo(UserRequestDto.UpdateUserInfoDto updateUserInfo) {
        this.name = updateUserInfo.getName();
        this.zipCode = updateUserInfo.getZipCode();
        this.address1 = updateUserInfo.getAddress1();
        this.address2 = updateUserInfo.getAddress2();
        this.phoneNumber = updateUserInfo.getPhoneNumber();
        this.birth = updateUserInfo.getBirth();
    }
}
