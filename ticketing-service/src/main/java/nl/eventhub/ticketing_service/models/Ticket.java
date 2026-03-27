package nl.eventhub.ticketing_service.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private long eventId;
    private LocalDateTime reservedAt;
    @Nullable
    private LocalDateTime completedAt;
    private TicketStatus status;

    public Ticket() {}

    public Ticket(Long userId, long eventId, LocalDateTime reservedAt, TicketStatus status) {
        this.userId = userId;
        this.eventId = eventId;
        this.reservedAt = reservedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    @Nullable
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(@Nullable LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
