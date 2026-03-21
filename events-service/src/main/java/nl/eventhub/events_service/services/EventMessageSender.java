package nl.eventhub.events_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eventhub.events_service.models.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventMessageSender {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${EVENT_UPDATED_QUEUE:event.updated}")
    private String eventUpdatedQueueName;

    public void sendEventUpdatedMessage(Event event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // Register JavaTimeModule for LocalDateTime support
            String jsonMessage = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(eventUpdatedQueueName, jsonMessage);
            System.out.println("Sent message to RabbitMQ queue " + eventUpdatedQueueName + ": " + jsonMessage);
        } catch (Exception e) {
            System.err.println("Error serializing event to JSON: " + e.getMessage());
        }
    }
}