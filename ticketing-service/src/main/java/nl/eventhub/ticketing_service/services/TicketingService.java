
package nl.eventhub.ticketing_service.services;

import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.models.TicketStatus;
import nl.eventhub.ticketing_service.repositories.TicketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketingService {

    private final TicketingRepository ticketingRepository;

    @Autowired
    public TicketingService(TicketingRepository ticketingRepository) {
        this.ticketingRepository = ticketingRepository;
    }

    public Ticket reserveTicket(long eventId, long userId) {
        Ticket ticket = new Ticket(userId, eventId, LocalDateTime.now(), TicketStatus.RESERVED);
        return ticketingRepository.save(ticket);
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
}
