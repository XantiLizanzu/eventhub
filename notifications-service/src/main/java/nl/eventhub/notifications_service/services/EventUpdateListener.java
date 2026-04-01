package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import nl.eventhub.notifications_service.models.Event;
import nl.eventhub.notifications_service.models.EventSubscription;
import nl.eventhub.notifications_service.repositories.EventSubscriptionRepository;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EventUpdateListener {
    
    @Value("${EVENT_UPDATED_QUEUE:event.updated}")
    private String eventUpdatedQueueName;
    
    private final EventSubscriptionRepository eventSubscriptionRepository;

    public EventUpdateListener(EventSubscriptionRepository eventSubscriptionRepository) {
        this.eventSubscriptionRepository = eventSubscriptionRepository;
    }
    
    @RabbitListener(queues = "${EVENT_UPDATED_QUEUE:event.updated}", ackMode = "MANUAL")
    public void handleEventUpdated(String jsonMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("Received event update from RabbitMQ queue " + eventUpdatedQueueName);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // For LocalDateTime support
            Event event = objectMapper.readValue(jsonMessage, Event.class);
            
            System.out.println("Notification service processing for event: " + event.getName() + "...");
            
            // Send updates to subscribed users
            sendEventUpdate(event);
            
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
    
    private void sendEventUpdate(Event event) {
        List<EventSubscription> subscriptions = eventSubscriptionRepository.findByEventId(event.getId());
        for (EventSubscription subscription : subscriptions) {
            System.out.println("Sending update to user " + subscription.getUserId() + " for event " + event.getId());
        }
    }
}