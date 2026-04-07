package nl.eventhub.notifications_service.controllers;

import nl.eventhub.notifications_service.models.EventSubscription;
import nl.eventhub.notifications_service.repositories.EventSubscriptionRepository;
import nl.eventhub.notifications_service.services.EventClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventSubscriptionControllerTest {

    @InjectMocks
    private EventSubscriptionController eventSubscriptionController;

    @Mock
    private EventSubscriptionRepository eventSubscriptionRepository;

    @Mock
    private EventClient eventClient;

    private EventSubscription testSubscription;

    @BeforeEach
    void setUp() {
        testSubscription = new EventSubscription(1L, 100L);
        testSubscription.setId(1L);
    }

    @Test
    void subscribeToEvent_shouldCreateNewSubscription() {
        // Arrange
        Long userId = 1L;
        Long eventId = 100L;
        
        when(eventClient.eventExists(eventId)).thenReturn(true);
        when(eventSubscriptionRepository.save(any(EventSubscription.class)))
            .thenReturn(testSubscription);

        // Act
        ResponseEntity<String> response = eventSubscriptionController.subscribeToEvent(userId, eventId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Successfully subscribed to event 100", response.getBody());
        verify(eventSubscriptionRepository, times(1)).save(any(EventSubscription.class));
    }

    @Test
    void unsubscribeFromEvent_shouldDeleteExistingSubscription() {
        // Arrange
        Long userId = 1L;
        Long eventId = 100L;
        
        when(eventSubscriptionRepository.findByUserIdAndEventId(userId, eventId))
            .thenReturn(testSubscription);

        // Act
        ResponseEntity<String> response = eventSubscriptionController.unsubscribeFromEvent(userId, eventId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Successfully unsubscribed from event 100", response.getBody());
        verify(eventSubscriptionRepository, times(1)).delete(testSubscription);
    }

    @Test
    void unsubscribeFromEvent_shouldReturnNotFoundForNonExistentSubscription() {
        // Arrange
        Long userId = 1L;
        Long eventId = 100L;
        
        when(eventSubscriptionRepository.findByUserIdAndEventId(userId, eventId))
            .thenReturn(null);

        // Act
        ResponseEntity<String> response = eventSubscriptionController.unsubscribeFromEvent(userId, eventId);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        verify(eventSubscriptionRepository, never()).delete(any(EventSubscription.class));
    }

    @Test
    void subscribeToEvent_shouldHandleNullValues() {
        // Arrange
        when(eventClient.eventExists(null)).thenReturn(true);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            eventSubscriptionController.subscribeToEvent(null, null);
        });
        verify(eventSubscriptionRepository, times(1)).save(any(EventSubscription.class));
    }

    @Test
    void subscribeToEvent_shouldReturnBadRequestWhenEventDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long eventId = 999L;
        
        when(eventClient.eventExists(eventId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = eventSubscriptionController.subscribeToEvent(userId, eventId);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Event with ID 999 does not exist", response.getBody());
        verify(eventSubscriptionRepository, never()).save(any(EventSubscription.class));
    }
}