package _c.shop.global.event.service;

public interface EmailEventPublisher {

    void publishEmailVerificationEvent(String email);
}
