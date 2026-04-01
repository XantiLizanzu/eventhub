package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import nl.eventhub.notifications_service.models.Event;
import nl.eventhub.notifications_service.models.EventSubscription;
import nl.eventhub.notifications_service.repositories.EventSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventUpdateListenerTest {

    @InjectMocks
    private EventUpdateListener eventUpdateListener;

    @Mock
    private Channel channel;

    @Mock
    private EventSubscriptionRepository eventSubscriptionRepository;

    private Event testEvent;
    private String testEventJson;

    @BeforeEach
    void setUp() throws Exception {
        testEvent = new Event(1L, "Test Event", "Test Description", LocalDateTime.now(), "Test Location", 10);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        testEventJson = objectMapper.writeValueAsString(testEvent);
    }

    @Test
    void handleEventUpdated_shouldProcessValidEventMessage() throws Exception {
        // Act
        eventUpdateListener.handleEventUpdated(testEventJson, channel, 1L);

        // Assert - The method should not throw any exceptions
        // Success is verified by reaching this point without exceptions
        verify(channel, times(1)).basicAck(1L, false);
    }

    @Test
    void handleEventUpdated_shouldHandleInvalidJsonGracefully() throws Exception {
        // Arrange
        String invalidJson = "{\"invalid\": \"json\", \"missing\": \"fields\"}";

        // Act
        eventUpdateListener.handleEventUpdated(invalidJson, channel, 1L);
        
        // Assert
        verify(channel, times(1)).basicNack(1L, false, true);
    }

    @Test
    void handleEventUpdated_shouldHandleEmptyMessageGracefully() {
        // Arrange
        String emptyMessage = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(emptyMessage, channel, 1L);
        });
    }

    @Test
    void handleEventUpdated_shouldHandleNullMessageGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(null, channel, 1L);
        });
    }

    @Test
    void handleEventUpdated_shouldHandleMalformedJsonGracefully() {
        // Arrange
        String malformedJson = "{\"name\": \"Test\", \"invalid\": \"json\", }";

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(malformedJson, channel, 1L);
        });
    }

    @Test
    void handleEventUpdated_shouldSendUpdatesToSubscribedUsers() throws Exception {
        // Arrange
        EventSubscription subscription1 = new EventSubscription(1L, 1L);
        EventSubscription subscription2 = new EventSubscription(2L, 1L);
        List<EventSubscription> subscriptions = Arrays.asList(subscription1, subscription2);
        
        when(eventSubscriptionRepository.findByEventId(1L)).thenReturn(subscriptions);

        // Act
        eventUpdateListener.handleEventUpdated(testEventJson, channel, 1L);

        // Assert
        verify(eventSubscriptionRepository, times(1)).findByEventId(1L);
        verify(channel, times(1)).basicAck(1L, false);
    }

    @Test
    void handleEventUpdated_shouldHandleEventWithNoSubscriptions() throws Exception {
        // Arrange
        when(eventSubscriptionRepository.findByEventId(1L)).thenReturn(List.of());

        // Act
        eventUpdateListener.handleEventUpdated(testEventJson, channel, 1L);

        // Assert
        verify(eventSubscriptionRepository, times(1)).findByEventId(1L);
        verify(channel, times(1)).basicAck(1L, false);
    }

    @Test
    void handleEventUpdated_shouldHandleRepositoryExceptionGracefully() throws Exception {
        // Arrange
        when(eventSubscriptionRepository.findByEventId(1L))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        eventUpdateListener.handleEventUpdated(testEventJson, channel, 1L);

        // Assert
        verify(channel, times(1)).basicNack(1L, false, true);
    }

    @Test
    void handleEventUpdated_shouldHandleChannelAckExceptionGracefully() throws Exception {
        // Arrange
        EventSubscription subscription = new EventSubscription(1L, 1L);
        when(eventSubscriptionRepository.findByEventId(1L))
            .thenReturn(List.of(subscription));
        doThrow(new IOException("Channel error")).when(channel).basicAck(1L, false);

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(testEventJson, channel, 1L);
        });
    }
}