package _c.shop.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DormantUserInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormant_user_info_id")
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
    private Boolean point;
    private LocalDateTime lastLoginAt;
    private Boolean isVerified;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
