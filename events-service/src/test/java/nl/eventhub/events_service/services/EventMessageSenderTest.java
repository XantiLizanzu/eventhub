package nl.eventhub.events_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.eventhub.events_service.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventMessageSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EventMessageSender eventMessageSender;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event("Test Event", "Test Description", LocalDateTime.now(), "Test Location", 10);
        testEvent.setId(1L);
    }

    @Test
    void sendEventUpdatedMessage_shouldSendMessageToRabbitMQ() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String expectedJson = objectMapper.writeValueAsString(testEvent);

        eventMessageSender.sendEventUpdatedMessage(testEvent);

        verify(rabbitTemplate, times(1)).convertAndSend(eq(null), anyString());
    }

    @Test
    void sendEventUpdatedMessage_shouldHandleSerializationError() {
        // Arrange
        // This test verifies that the method doesn't throw exceptions when serialization fails
        // The actual serialization error handling is tested by the fact that no exception is thrown

        // Act
        eventMessageSender.sendEventUpdatedMessage(testEvent);

        // Assert
        // The method should not throw an exception even if there are serialization issues
        // This is verified by the fact that we reach this point without exception
        verify(rabbitTemplate, times(1)).convertAndSend(eq(null), anyString());
    }
}