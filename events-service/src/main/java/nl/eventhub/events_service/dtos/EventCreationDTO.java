package nl.eventhub.events_service.dtos;

import nl.eventhub.events_service.models.Event;

import java.time.LocalDateTime;

public class EventCreationDTO extends Event {
    public EventCreationDTO(String name, String description, LocalDateTime dateTime, String location, int totalTicketCount) {
        super(name, description, dateTime, location, totalTicketCount);
    }

    public EventCreationDTO() {
        super();
    }
}