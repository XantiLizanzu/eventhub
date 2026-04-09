package nl.eventhub.payments_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eventhub.payments_service.models.Payment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${PAYMENT_SUCCEEDED_QUEUE:payment.succeeded}")
    private String paymentSucceededQueueName;

    public void sendPaymentSucceededQueueName(Payment payment) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // Register JavaTimeModule for LocalDateTime support
            String jsonMessage = objectMapper.writeValueAsString(payment);
            rabbitTemplate.convertAndSend(paymentSucceededQueueName, jsonMessage);
            System.out.println("Sent message to RabbitMQ queue " + paymentSucceededQueueName + ": " + jsonMessage);
        } catch (Exception e) {
            System.err.println("Error serializing event to JSON: " + e.getMessage());
        }
    }
}
