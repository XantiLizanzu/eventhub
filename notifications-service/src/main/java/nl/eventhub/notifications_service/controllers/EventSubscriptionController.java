package nl.eventhub.notifications_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.notifications_service.models.EventSubscription;
import nl.eventhub.notifications_service.repositories.EventSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Event Subscriptions")
@RequestMapping("/api/subscriptions")
public class EventSubscriptionController {

    @Autowired
    private EventSubscriptionRepository eventSubscriptionRepository;

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe to an event", description = "Subscribes a user to receive notifications for a specific event")
    public ResponseEntity<String> subscribeToEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        EventSubscription subscription = new EventSubscription(userId, eventId);
        eventSubscriptionRepository.save(subscription);
        return ResponseEntity.ok("Successfully subscribed to event " + eventId);
    }

    @PostMapping("/unsubscribe")
    @Operation(summary = "Unsubscribe from an event", description = "Removes a user's subscription to receive notifications for a specific event")
    public ResponseEntity<String> unsubscribeFromEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        EventSubscription subscription = eventSubscriptionRepository.findByUserIdAndEventId(userId, eventId);
        if (subscription != null) {
            eventSubscriptionRepository.delete(subscription);
            return ResponseEntity.ok("Successfully unsubscribed from event " + eventId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}