package nl.eventhub.events_service.controllers;

import nl.eventhub.events_service.models.Event;
import nl.eventhub.events_service.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event testEvent;
    private EventController.EventCreationDTO creationDTO;

    @BeforeEach
    void setUp() {
        testEvent = new Event("Test Event", "Test Description", LocalDateTime.now(), "Test Location");
        testEvent.setId(1L);
        creationDTO = new EventController.EventCreationDTO("Test Event", "Test Description", LocalDateTime.now(), "Test Location");
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents() {
        // Arrange
        Event event2 = new Event("Event 2", "Description 2", LocalDateTime.now(), "Location 2");
        when(eventService.getAllEvents()).thenReturn(Arrays.asList(testEvent, event2));

        // Act
        List<Event> result = eventController.getAllEvents();

        // Assert
        assertEquals(2, result.size());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void getEventById_shouldReturnEventWhenFound() {
        // Arrange
        when(eventService.getEventById(anyLong())).thenReturn(Optional.of(testEvent));

        // Act
        ResponseEntity<Event> result = eventController.getEventById(1L);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testEvent, result.getBody());
        verify(eventService, times(1)).getEventById(1L);
    }

    @Test
    void getEventById_shouldReturnNotFoundWhenNotFound() {
        // Arrange
        when(eventService.getEventById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Event> result = eventController.getEventById(1L);

        // Assert
        assertTrue(result.getStatusCode().is4xxClientError());
        assertNull(result.getBody());
        verify(eventService, times(1)).getEventById(1L);
    }

    @Test
    void createEvent_shouldCreateAndReturnEvent() {
        // Arrange
        when(eventService.createEvent(any(Event.class))).thenReturn(testEvent);

        // Act
        Event result = eventController.createEvent(creationDTO);

        // Assert
        assertEquals(testEvent.getName(), result.getName());
        verify(eventService, times(1)).createEvent(any(Event.class));
    }

    @Test
    void updateEvent_shouldUpdateAndReturnEvent() {
        // Arrange
        Event updatedEvent = new Event("Updated Name", "Updated Description", LocalDateTime.now(), "Updated Location");
        when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(updatedEvent);

        // Act
        ResponseEntity<Event> result = eventController.updateEvent(1L, updatedEvent);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(updatedEvent, result.getBody());
        verify(eventService, times(1)).updateEvent(eq(1L), any(Event.class));
    }

    @Test
    void deleteEvent_shouldReturnNoContent() {
        // Arrange
        doNothing().when(eventService).deleteEvent(anyLong());

        // Act
        ResponseEntity<Void> result = eventController.deleteEvent(1L);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        verify(eventService, times(1)).deleteEvent(1L);
    }
}