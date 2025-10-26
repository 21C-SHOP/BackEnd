package _c.shop.user.repository;

import _c.shop.user.domain.OauthId;
import _c.shop.user.domain.User;
import _c.shop.user.domain.UserRole;
import _c.shop.user.vo.UserInfoVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByOauthId(OauthId oauthId);

    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    Optional<UserRole> findRoleByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT new _c.shop.user.vo.UserInfoVO(u.email, u.name, u.zipCode, u.address1, u.address2, u.phoneNumber, u.birth) " +
            "FROM User u " +
            "WHERE u.email = :email")
    Optional<UserInfoVO> findUserInfoVOByEmail(@Param("email") String email);
}
