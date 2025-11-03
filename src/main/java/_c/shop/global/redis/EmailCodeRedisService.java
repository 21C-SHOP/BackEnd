package _c.shop.global.redis;

import _c.shop.user.converter.UserConverter;
import _c.shop.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailCodeRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserConverter userConverter;

    /**
     * 3분 내로 이메일 코드 인증 받기
     */
    public void saveEmailCode(String email, String verificationCode) {
        redisTemplate.opsForValue().set(email, verificationCode, 3, TimeUnit.MINUTES);
    }

    public UserResponseDto.VerifyEmailDto verifyEmailCode(String email, String verificationCode) {
        String codeInRedis = (String) redisTemplate.opsForValue().get(email);
        log.info("사용자가 입력한 인증 코드: {}", verificationCode);
        log.info("Redis에서 조회한 인증 코드: {}", codeInRedis);
        if (codeInRedis == null || !codeInRedis.equals(verificationCode)) {
            return userConverter.toVerifyEmailDto(false);
        }
        redisTemplate.delete(email);
        return userConverter.toVerifyEmailDto(true);
    }
}
