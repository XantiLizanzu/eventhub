package nl.eventhub.notifications_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${EVENT_UPDATED_QUEUE:event.updated}")
    private String eventUpdatedQueueName;

    @Bean
    public Queue eventUpdatedQueue() {
        return new Queue(eventUpdatedQueueName, false);
    }
}