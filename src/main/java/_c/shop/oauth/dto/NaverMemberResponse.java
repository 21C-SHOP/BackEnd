package _c.shop.oauth.dto;

import _c.shop.user.domain.OauthId;
import _c.shop.user.domain.User;
import _c.shop.user.domain.UserRole;
import _c.shop.user.domain.UserStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static _c.shop.oauth.OauthServerType.NAVER;


@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

    public User toDomain() {
        return User.builder()
                .oauthId(new OauthId(String.valueOf(response.id), NAVER))
                .name(response.name)
                .email(response.email)
                .point(0D)
                .isVerified(true)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(java.time.LocalDateTime.now())
                .role(UserRole.USER)
                .build();
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Response(
            String id,
            String nickname,
            String name,
            String email,
            String gender,
            String age,
            String birthday,
            String profileImage,
            String birthyear,
            String mobile
    ) {
    }
}
