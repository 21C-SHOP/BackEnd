package _c.shop.oauth.client;


import _c.shop.oauth.OauthServerType;
import _c.shop.user.domain.User;

/**
 * AuthCode를 통해 OauthMember 객체 생성 (회원 정보 조회)
 */

public interface OauthUserClient {

    OauthServerType supportServer();

    User fetch(String code);
}