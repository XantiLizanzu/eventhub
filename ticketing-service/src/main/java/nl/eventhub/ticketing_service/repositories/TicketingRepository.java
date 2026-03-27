package nl.eventhub.ticketing_service.repositories;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketingRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventIdAndStatusIn(long eventId, List<TicketStatus> statuses);
    Optional<Ticket> findByIdAndEventId(long ticketId, long eventId);
    List<Ticket> findByUserId(long userId);
}
