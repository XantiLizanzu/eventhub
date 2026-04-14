package nl.eventhub.ticketing_service.services;

import nl.eventhub.ticketing_service.models.TicketStatus;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.repositories.TicketingRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketingServiceTest {
    @Mock
    private TicketingRepository ticketingRepository;

    @InjectMocks
    private TicketingService ticketingService;

    private Ticket testTicket;
    private final long eventId = 1L;
    private final long userId = 100L;
    private final long ticketId = 10L;

    @BeforeEach
    void setUp() {
        testTicket = new Ticket(userId, eventId, LocalDateTime.now(), TicketStatus.RESERVED);
    }

//    @Test
//    void reserveTicket_shouldCreateTicketWithReservedStatus() {
//        // Arrange
//        when(ticketingRepository.save(any(Ticket.class))).thenReturn(testTicket);
//
//        // Act
//        Ticket result = ticketingService.reserveTicket(eventId, userId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(TicketStatus.RESERVED, result.getStatus());
//        assertEquals(eventId, result.getEventId());
//        assertEquals(userId, result.getUserId());
//        verify(ticketingRepository, times(1)).save(any(Ticket.class));
//    }

    @Test
    void unreserveTicket_shouldSetStatusToUnreserved() {
        // Arrange
        when(ticketingRepository.findById(ticketId)).thenReturn(Optional.of(testTicket));
        when(ticketingRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Ticket> result = ticketingService.unreserveTicket(ticketId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(TicketStatus.UNRESERVED, result.get().getStatus());
        verify(ticketingRepository, times(1)).findById(ticketId);
        verify(ticketingRepository, times(1)).save(any(Ticket.class));
    }

//    @Test
//    void completeTicket_shouldSetStatusToCompletedAndSetCompletedAt() {
//        // Arrange
//        when(ticketingRepository.findById(ticketId)).thenReturn(Optional.of(testTicket));
//        when(ticketingRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        Ticket result = ticketingService.completeTicket(ticketId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(TicketStatus.COMPLETED, result.getStatus());
//        assertNotNull(result.getCompletedAt());
//        verify(ticketingRepository, times(1)).findById(ticketId);
//        verify(ticketingRepository, times(1)).save(any(Ticket.class));
//    }
//
//    @Test
//    void completeTicket_shouldThrowExceptionWhenTicketNotFound() {
//        // Arrange
//        when(ticketingRepository.findById(ticketId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(TicketCantBeCompletedException.class, () -> {
//            ticketingService.completeTicket(ticketId);
//        });
//
//        verify(ticketingRepository, times(1)).findById(ticketId);
//    }

    @Test
    void getAvailability_shouldReturnCountOfReservedAndCompletedTickets() {
        // Arrange
        List<Ticket> tickets = Arrays.asList(
                new Ticket(1L, eventId, LocalDateTime.now(), TicketStatus.RESERVED),
                new Ticket(2L, eventId, LocalDateTime.now(), TicketStatus.COMPLETED)
        );
        when(ticketingRepository.findByEventIdAndStatusIn(eq(eventId), anyList())).thenReturn(tickets);

        // Act
        int result = ticketingService.getAvailability(eventId);

        // Assert
        assertEquals(2, result);
        verify(ticketingRepository, times(1)).findByEventIdAndStatusIn(eq(eventId), anyList());
    }

    @Test
    void getTicket_shouldReturnTicketForEventAndId() {
        // Arrange
        when(ticketingRepository.findByIdAndEventId(ticketId, eventId)).thenReturn(Optional.of(testTicket));

        // Act
        Optional<Ticket> result = ticketingService.getTicket(eventId, ticketId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testTicket, result.get());
        verify(ticketingRepository, times(1)).findByIdAndEventId(ticketId, eventId);
    }

    @Test
    void getTicketsForUser_shouldReturnListOfTickets() {
        // Arrange
        List<Ticket> tickets = List.of(testTicket);
        when(ticketingRepository.findByUserId(userId)).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketingService.getTicketsForUser(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testTicket, result.getFirst());
        verify(ticketingRepository, times(1)).findByUserId(userId);
    }

//    @Test
//    void cleanupExpiredReservations_shouldUnreserveExpiredTickets() {
//        // Arrange
//        Ticket expiredTicket = new Ticket(userId, eventId, LocalDateTime.now().minusSeconds(20), TicketStatus.RESERVED);
//        when(ticketingRepository.findByStatusAndReservedAtBefore(eq(TicketStatus.RESERVED), any(LocalDateTime.class)))
//                .thenReturn(List.of(expiredTicket));
//
//        // Act
//        ticketingService.cleanupExpiredReservations();
//
//        // Assert
//        assertEquals(TicketStatus.UNRESERVED, expiredTicket.getStatus());
//        verify(ticketingRepository, times(1)).save(expiredTicket);
//    }
}
