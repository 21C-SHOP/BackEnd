package _c.shop.jwt;

import _c.shop.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal {

    private String email;
    private UserRole role;
}
