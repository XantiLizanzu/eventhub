package nl.eventhub.ticketing_service.models;

import java.time.LocalDateTime;

public class EventResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private int totalTicketCount;
    private int availableTickets;

    // Constructors
    public EventResponseDTO() {
    }

    public EventResponseDTO(String name, String description, LocalDateTime dateTime, String location, int totalTicketCount, int availableTickets) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.totalTicketCount = totalTicketCount;
        this.availableTickets = availableTickets;
    }

    // Getters and Setters
    public int getAvailableTickets() {
        return availableTickets;
    }
}
