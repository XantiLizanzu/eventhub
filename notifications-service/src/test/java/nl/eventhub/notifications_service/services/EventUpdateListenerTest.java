package nl.eventhub.notifications_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eventhub.notifications_service.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventUpdateListenerTest {

    @InjectMocks
    private EventUpdateListener eventUpdateListener;

    private Event testEvent;
    private String testEventJson;

    @BeforeEach
    void setUp() throws Exception {
        testEvent = new Event(1L, "Test Event", "Test Description", LocalDateTime.now(), "Test Location");
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        testEventJson = objectMapper.writeValueAsString(testEvent);
    }

    @Test
    void handleEventUpdated_shouldProcessValidEventMessage() {
        // Act
        eventUpdateListener.handleEventUpdated(testEventJson);

        // Assert - The method should not throw any exceptions
        // Success is verified by reaching this point without exceptions
    }

    @Test
    void handleEventUpdated_shouldHandleInvalidJsonGracefully() {
        // Arrange
        String invalidJson = "{\"invalid\": \"json\", \"missing\": \"fields\"}";

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(invalidJson);
        });
    }

    @Test
    void handleEventUpdated_shouldHandleEmptyMessageGracefully() {
        // Arrange
        String emptyMessage = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(emptyMessage);
        });
    }

    @Test
    void handleEventUpdated_shouldHandleNullMessageGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(null);
        });
    }

    @Test
    void handleEventUpdated_shouldHandleMalformedJsonGracefully() {
        // Arrange
        String malformedJson = "{\"name\": \"Test\", \"invalid\": \"json\", }";

        // Act & Assert
        assertDoesNotThrow(() -> {
            eventUpdateListener.handleEventUpdated(malformedJson);
        });
    }
}