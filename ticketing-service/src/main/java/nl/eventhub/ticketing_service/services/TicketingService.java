
package nl.eventhub.ticketing_service.services;

import jakarta.annotation.PostConstruct;
import nl.eventhub.ticketing_service.models.EventResponseDTO;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.models.TicketStatus;
import nl.eventhub.ticketing_service.repositories.TicketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketingService {

    private final TicketingRepository ticketingRepository;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private EventClient eventClient;

    @Autowired
    public TicketingService(TicketingRepository ticketingRepository) {
        this.ticketingRepository = ticketingRepository;
    }

    @PostConstruct
    public void initializeSampleEvents() {

        if (ticketingRepository.count() == 0) {
            ticketingRepository.saveAll(List.of(
                            new Ticket(1L, 1,
                                    LocalDateTime.of(2026, 4, 8, 17, 0),
                                    TicketStatus.COMPLETED)
                    )
            );
        }
    }

    public Optional<Ticket> reserveTicket(long eventId, long userId) {
        // Fetch the event details
        ResponseEntity<EventResponseDTO> eventResponse = eventClient.getEventResponse(eventId);

        // Check if the response is an error
        if (eventResponse.getStatusCode().isError()) {
            return Optional.empty();
        }

        // Check if there are no available tickets
        EventResponseDTO event = eventResponse.getBody();
        if (event == null || event.getAvailableTickets() <= 0) {
            return Optional.empty();
        }

        // Proceed with ticket creation and reservation
        Ticket ticket = new Ticket(userId, eventId, LocalDateTime.now(), TicketStatus.RESERVED);
        Ticket newTicket = ticketingRepository.save(ticket);
        paymentClient.pay(newTicket.getId());

        return Optional.of(newTicket);
    }

    public Optional<Ticket> unreserveTicket(long ticketId) {
        return ticketingRepository.findById(ticketId).map(ticket -> {
            ticket.setStatus(TicketStatus.UNRESERVED);
            return ticketingRepository.save(ticket);
        });
    }

    public Optional<Ticket> completeTicket(long ticketId) {
        return ticketingRepository.findById(ticketId).map(ticket -> {
            ticket.setStatus(TicketStatus.COMPLETED);
            ticket.setCompletedAt(LocalDateTime.now());
            return ticketingRepository.save(ticket);
        });
    }

    public int getAvailability(long eventId) {
        List<Ticket> tickets = ticketingRepository.findByEventIdAndStatusIn(
                eventId, List.of(TicketStatus.RESERVED, TicketStatus.COMPLETED));
        return tickets.size();
    }

    public Optional<Ticket> getTicket(long eventId, long ticketId) {
        return ticketingRepository.findByIdAndEventId(ticketId, eventId);
    }

    public List<Ticket> getTicketsForUser(long userId) {
        return ticketingRepository.findByUserId(userId);
    }

    @Scheduled(fixedRate = 5000)
    public void cleanupExpiredReservations() {
        List<Ticket> expiredTickets = ticketingRepository.findAll().stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.RESERVED)
                .filter(ticket -> LocalDateTime.now().isAfter(ticket.getReservedAt().plusSeconds(15)))
                .toList();

        for (Ticket ticket : expiredTickets) {
            ticket.setStatus(TicketStatus.UNRESERVED);
            ticketingRepository.save(ticket);
        }
    }
}
