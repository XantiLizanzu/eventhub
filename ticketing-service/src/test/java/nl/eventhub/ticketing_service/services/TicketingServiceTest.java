package nl.eventhub.ticketing_service.services;

import nl.eventhub.ticketing_service.models.EventResponseDTO;
import nl.eventhub.ticketing_service.models.TicketStatus;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.repositories.TicketingRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketingServiceTest {
    @Mock
    private TicketingRepository ticketingRepository;

    @Mock
    private EventClient eventClient;

    @Mock
    private PaymentClient paymentClient;

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

    @Test
    void reserveTicket_shouldCreateTicketWithReservedStatus() {
        // Arrange
        when(ticketingRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(eventClient.getEventResponse(anyLong())).thenReturn(ResponseEntity.ok(new EventResponseDTO("Test",
                "Test", LocalDateTime.now(), "Test", 1, 1)));

        // Act
        Optional<Ticket> result = ticketingService.reserveTicket(eventId, userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(TicketStatus.RESERVED, result.get().getStatus());
        assertEquals(eventId, result.get().getEventId());
        assertEquals(userId, result.get().getUserId());
        verify(ticketingRepository, times(1)).save(any(Ticket.class));
        verify(eventClient, times(1)).getEventResponse(eventId);
    }

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

    @Test
    void completeTicket_shouldSetStatusToCompletedAndSetCompletedAt() {
        // Arrange
        when(ticketingRepository.findById(ticketId)).thenReturn(Optional.of(testTicket));
        when(ticketingRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Ticket> result = ticketingService.completeTicket(ticketId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(TicketStatus.COMPLETED, result.get().getStatus());
        assertNotNull(result.get().getCompletedAt());
        verify(ticketingRepository, times(1)).findById(ticketId);
        verify(ticketingRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void completeTicket_shouldReturnEmptyWhenTicketNotFound() {
        // Arrange
        when(ticketingRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> result = ticketingService.completeTicket(ticketId);

        // Assert
        assertTrue(result.isEmpty());
        verify(ticketingRepository, times(1)).findById(ticketId);
    }

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

    @Test
    void cleanupExpiredReservations_shouldUnreserveExpiredTickets() {
        // Arrange
        Ticket expiredTicket = new Ticket(userId, eventId, LocalDateTime.now().minusSeconds(20), TicketStatus.RESERVED);
        when(ticketingRepository.findAll()).thenReturn(List.of(expiredTicket));
        when(ticketingRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ticketingService.cleanupExpiredReservations();

        // Assert
        assertEquals(TicketStatus.UNRESERVED, expiredTicket.getStatus());
        verify(ticketingRepository, times(1)).save(expiredTicket);
    }


}
