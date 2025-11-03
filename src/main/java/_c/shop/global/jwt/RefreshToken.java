package _c.shop.global.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    private String email;

    // refresh : Object로 Redis에 저장
    private String refresh;

    private Integer expiration;
}