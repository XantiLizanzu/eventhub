package nl.eventhub.payments_service.models;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;

    private Long userId;
    private long eventId;
    private LocalDateTime reservedAt;
    @Nullable
    private LocalDateTime completedAt;
    private TicketStatus status;
}
