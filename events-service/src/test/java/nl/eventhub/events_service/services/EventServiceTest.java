package nl.eventhub.events_service.services;

import nl.eventhub.events_service.models.Event;
import nl.eventhub.events_service.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMessageSender eventMessageSender;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event("Test Event", "Test Description", LocalDateTime.now(), "Test Location");
        testEvent.setId(1L);
    }

    @Test
    void getAllEvents_shouldReturnAllEvents() {
        // Arrange
        Event event2 = new Event("Event 2", "Description 2", LocalDateTime.now(), "Location 2");
        when(eventRepository.findAll()).thenReturn(Arrays.asList(testEvent, event2));

        // Act
        List<Event> result = eventService.getAllEvents();

        // Assert
        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getEventById_shouldReturnEventWhenFound() {
        // Arrange
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));

        // Act
        Optional<Event> result = eventService.getEventById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEvent.getName(), result.get().getName());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getEventById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Event> result = eventService.getEventById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void createEvent_shouldSaveAndReturnEvent() {
        // Arrange
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        Event result = eventService.createEvent(testEvent);

        // Assert
        assertEquals(testEvent.getName(), result.getName());
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void updateEvent_shouldUpdateAndReturnEvent() {
        // Arrange
        Event updatedDetails = new Event("Updated Name", "Updated Description", LocalDateTime.now(), "Updated Location");
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        doNothing().when(eventMessageSender).sendEventUpdatedMessage(any(Event.class));

        // Act
        Event result = eventService.updateEvent(1L, updatedDetails);

        // Assert
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(testEvent);
        verify(eventMessageSender, times(1)).sendEventUpdatedMessage(testEvent);
    }

    @Test
    void updateEvent_shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.updateEvent(1L, testEvent);
        });

        assertEquals("Event not found with id: 1", exception.getMessage());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent_shouldDeleteEvent() {
        // Arrange
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        doNothing().when(eventRepository).delete(any(Event.class));

        // Act
        eventService.deleteEvent(1L);

        // Assert
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).delete(testEvent);
    }

    @Test
    void deleteEvent_shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.deleteEvent(1L);
        });

        assertEquals("Event not found with id: 1", exception.getMessage());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, never()).delete(any(Event.class));
    }
}