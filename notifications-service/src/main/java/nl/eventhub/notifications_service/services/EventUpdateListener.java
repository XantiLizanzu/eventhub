package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import nl.eventhub.notifications_service.models.Event;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventUpdateListener {
    
    @Value("${EVENT_UPDATED_QUEUE:event.updated}")
    private String eventUpdatedQueueName;
    
    @RabbitListener(queues = "${EVENT_UPDATED_QUEUE:event.updated}", ackMode = "MANUAL")
    public void handleEventUpdated(String jsonMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("Received message from RabbitMQ queue " + eventUpdatedQueueName + ": " + jsonMessage);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // For LocalDateTime support
            Event event = objectMapper.readValue(jsonMessage, Event.class);
            
            System.out.println("Notification service processing for event: " + event.getName() + "...");
            
            // Simulate notification processing
            Thread.sleep(2000);
            System.out.println("Notification processing completed for event " + event.getId());
            
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