package nl.eventhub.ticketing_service.controllers;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.models.TicketStatus;
import nl.eventhub.ticketing_service.services.TicketingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketingControllerTest {

    @Mock
    private TicketingService ticketingService;

    @InjectMocks
    private TicketingController ticketingController;

    private Ticket testTicket;
    private final long eventId = 1L;
    private final long userId = 100L;
    private final long ticketId = 10L;

    @BeforeEach
    void setUp() {
        testTicket = new Ticket(userId, eventId, LocalDateTime.now(), TicketStatus.RESERVED);
    }

//    @Test
//    void reserveTicket_shouldReturnReservedTicket() {
//        // Arrange
//        when(ticketingService.reserveTicket(anyLong(), anyLong())).thenReturn(testTicket);
//
//        // Act
//        ResponseEntity<Ticket> result = ticketingController.reserveTicket(eventId, userId);
//
//        // Assert
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        assertEquals(testTicket, result.getBody());
//        verify(ticketingService, times(1)).reserveTicket(eventId, userId);
//    }

    @Test
    void reserveTicket_shouldReturnNotFoundWhenTicketIsNull() {
        // Arrange
        when(ticketingService.reserveTicket(anyLong(), anyLong())).thenReturn(null);

        // Act
        ResponseEntity<Ticket> result = ticketingController.reserveTicket(eventId, userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(ticketingService, times(1)).reserveTicket(eventId, userId);
    }

    @Test
    void unreserveTicket_shouldReturnUnreservedTicketWhenFound() {
        // Arrange
        testTicket.setStatus(TicketStatus.UNRESERVED);
        when(ticketingService.unreserveTicket(anyLong())).thenReturn(Optional.of(testTicket));

        // Act
        ResponseEntity<Ticket> result = ticketingController.unreserveTicket(ticketId);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testTicket, result.getBody());
        verify(ticketingService, times(1)).unreserveTicket(ticketId);
    }

    @Test
    void unreserveTicket_shouldReturnNotFoundWhenTicketMissing() {
        // Arrange
        when(ticketingService.unreserveTicket(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Ticket> result = ticketingController.unreserveTicket(ticketId);

        // Assert
        assertTrue(result.getStatusCode().is4xxClientError());
        verify(ticketingService, times(1)).unreserveTicket(ticketId);
    }

//    @Test
//    void completeTicket_shouldReturnCompletedTicketWhenFound() {
//        // Arrange
//        testTicket.setStatus(TicketStatus.COMPLETED);
//        when(ticketingService.completeTicket(anyLong())).thenReturn(testTicket);
//
//        // Act
//        ResponseEntity<Ticket> result = ticketingController.completeTicket(ticketId);
//
//        // Assert
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        assertEquals(testTicket, result.getBody());
//        verify(ticketingService, times(1)).completeTicket(ticketId);
//    }

//    @Test
//    void completeTicket_shouldThrowNotFoundWhenTicketMissing() {
//        // Arrange
//        when(ticketingService.completeTicket(anyLong()))
//                .thenThrow(new TicketCantBeCompletedException("Ticket not found or not reserved"));
//
//        // Act & Assert
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            ticketingController.completeTicket(ticketId);
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        verify(ticketingService, times(1)).completeTicket(ticketId);
//    }

    @Test
    void getAvailability_shouldReturnCount() {
        // Arrange
        int availability = 5;
        when(ticketingService.getAvailability(anyLong())).thenReturn(availability);

        // Act
        ResponseEntity<Integer> result = ticketingController.getAvailability(eventId);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(availability, result.getBody());
        verify(ticketingService, times(1)).getAvailability(eventId);
    }

    @Test
    void getTicket_shouldReturnTicketWhenFound() {
        // Arrange
        when(ticketingService.getTicket(anyLong(), anyLong())).thenReturn(Optional.of(testTicket));

        // Act
        ResponseEntity<Ticket> result = ticketingController.getTicket(eventId, ticketId);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testTicket, result.getBody());
        verify(ticketingService, times(1)).getTicket(eventId, ticketId);
    }

    @Test
    void getTicket_shouldReturnNotFoundWhenMissing() {
        // Arrange
        when(ticketingService.getTicket(anyLong(), anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Ticket> result = ticketingController.getTicket(eventId, ticketId);

        // Assert
        assertTrue(result.getStatusCode().is4xxClientError());
        verify(ticketingService, times(1)).getTicket(eventId, ticketId);
    }

    @Test
    void getTicketsForUser_shouldReturnListOfTickets() {
        // Arrange
        List<Ticket> tickets = Collections.singletonList(testTicket);
        when(ticketingService.getTicketsForUser(anyLong())).thenReturn(tickets);

        // Act
        ResponseEntity<List<Ticket>> result = ticketingController.getTicketsForUser(userId);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(tickets, result.getBody());
        verify(ticketingService, times(1)).getTicketsForUser(userId);
    }
}
