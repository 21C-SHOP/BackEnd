package _c.shop.global.event.converter;

import _c.shop.global.event.dto.EmailVerificationEvent;
import org.springframework.stereotype.Component;

@Component
public class RabbitConverter {

    public EmailVerificationEvent toSendEmailVerificationDto(String email) {
        String code = String.format("%06d", (int)(Math.random() * 1000000));
        return EmailVerificationEvent.builder()
                .email(email)
                .verificationCode(code)
                .build();
    }
}
