package _c.shop.user.service;

import _c.shop.jwt.UserPrincipal;
import _c.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public Optional<UserPrincipal> getUserPrincipalByEmail(String email) {
        return userRepository.findRoleByEmail(email)
                .map(role -> UserPrincipal.builder()
                        .email(email)
                        .role(role)
                        .build());
    }
}
