package _c.shop.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    // --- 교환기 (분류 센터) - 공통으로 사용 ---
    private static final String EXCHANGE_NAME = "notification.exchange"; // 분류 센터

    // --- 이메일 인증용 ---
    private static final String EMAIL_QUEUE_NAME = "email.verification.queue"; // MQ 이름
    private static final String EMAIL_ROUTING_KEY = "email.verification"; // 컨슈머 식별자

    // --- 결제 성공 알림용 ---

    // 메시지를 JSON으로 변환해줄 MessageConverter Bean 등록
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter); // JSON 컨버터 설정

        RetryTemplate retryTemplate = new RetryTemplate();

        // 1. 재시도 간격 설정 (예: 1초, 2초, 4초...)
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 1초
        backOffPolicy.setMultiplier(2.0); // 2배씩
        backOffPolicy.setMaxInterval(5000); // 최대 5초

        // 2. 재시도 횟수 설정 (예: 최대 3번)
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }

    // 교환기 Bean
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // --- "이메일 인증" 큐와 바인딩 (기존) ---
    @Bean
    public Queue emailVerificationQueue() {
        return new Queue(EMAIL_QUEUE_NAME);
    }

    @Bean
    public Binding emailVerificationBinding(Queue emailVerificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(emailVerificationQueue)
                .to(notificationExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    // --- 👇 "결제 성공 알림" 큐와 바인딩 (신규 추가!) ---
}
