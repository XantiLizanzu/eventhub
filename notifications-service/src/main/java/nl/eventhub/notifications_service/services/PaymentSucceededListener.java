package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import nl.eventhub.notifications_service.models.Payment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentSucceededListener {

    @Value("${PAYMENT_SUCCEEDED_QUEUE:payment.succeeded}")
    private String paymentSucceededQueueName;

    @RabbitListener(queues = "${PAYMENT_SUCCEEDED_QUEUE:payment.succeeded}", ackMode = "MANUAL")
    public void handlePaymentSucceeded(String jsonMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("Received payment succeeded from RabbitMQ queue " + paymentSucceededQueueName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // For LocalDateTime support
            Payment payment = objectMapper.readValue(jsonMessage, Payment.class);

            System.out.println("Sending update for ticket " + payment.getId());

            // Manual acknowledgment on success
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.err.println("Error processing event update message: " + e.getMessage());
            try {
                // Reject and requeue the message on failure
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                System.err.println("Failed to nack message: " + ioException.getMessage());
            }
        }
    }
}
