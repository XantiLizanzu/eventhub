package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import nl.eventhub.notifications_service.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventUpdateListenerTest {

    @InjectMocks
    private EventUpdateListener eventUpdateListener;

    @Mock
    private Channel channel;

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
}