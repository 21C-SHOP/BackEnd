package _c.shop.oauth.dto;

import _c.shop.user.domain.OauthId;
import _c.shop.user.domain.User;
import _c.shop.user.domain.UserRole;
import _c.shop.user.domain.UserStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

import static _c.shop.oauth.OauthServerType.GOOGLE;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String email,
        boolean verified_email,
        String name,
        String given_name,
        String family_name,
        String picture,
        String locale
) {

    public User toDomain() {
        return User.builder()
                .oauthId(new OauthId(id, GOOGLE))
                .email(email)
                .name(name)
                .point(0D)
                .isVerified(true)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .role(UserRole.USER)
                .build();
    }
}
