package _c.shop.global.event.service;

import _c.shop.global.event.converter.RabbitConverter;
import _c.shop.global.event.dto.EmailVerificationEvent;
import _c.shop.global.redis.EmailCodeRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQEmailEventPublisherImpl implements EmailEventPublisher {

    private final RabbitConverter rabbitConverter;
    private final RabbitTemplate rabbitTemplate;
    private final EmailCodeRedisService emailCodeRedisService;

    private static final String EXCHANGE_NAME = "notification.exchange";
    private static final String ROUTING_KEY = "email.verification";

    @Override
    public void publishEmailVerificationEvent(String email) {
        EmailVerificationEvent event = rabbitConverter.toSendEmailVerificationDto(email);
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
            emailCodeRedisService.saveEmailCode(email, event.getVerificationCode());
            log.info("이메일 인증 이벤트 발행 성공, email: {}", email);
        } catch (Exception e) {
            log.error("이메일 인증 이벤트 발행 실패, email: {}, error: {}", email, e.getMessage());
            // 재시도가 전부 실패했을 때 관리자 알림 정도?
        }
    }
}
