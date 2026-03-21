package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eventhub.notifications_service.models.Event;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventUpdateListener {
    
    @Value("${EVENT_UPDATED_QUEUE:event.updated}")
    private String eventUpdatedQueueName;
    
    @RabbitListener(queues = "${EVENT_UPDATED_QUEUE:event.updated}")
    public void handleEventUpdated(String jsonMessage) {
        System.out.println("Received message from RabbitMQ queue " + eventUpdatedQueueName + ": " + jsonMessage);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // For LocalDateTime support
            Event event = objectMapper.readValue(jsonMessage, Event.class);
            
            System.out.println("Notification service processing for event: " + event.getName() + "...");
            
            // Simulate notification processing
            Thread.sleep(2000);
            System.out.println("Notification processing completed for event " + event.getId());
        } catch (Exception e) {
            System.err.println("Error processing event update message: " + e.getMessage());
        }
    }
}