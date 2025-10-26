package _c.shop.user.service;

import _c.shop.jwt.UserPrincipal;

import java.util.Optional;

public interface UserQueryService {

    Optional<UserPrincipal> getUserPrincipalByEmail(String email);
}
