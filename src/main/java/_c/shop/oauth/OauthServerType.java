package _c.shop.oauth;

import java.util.Locale;

public enum OauthServerType {

    GENERAL,
    KAKAO,
    NAVER,
    GOOGLE;

    public static OauthServerType fromName(final String type) {
        return OauthServerType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}