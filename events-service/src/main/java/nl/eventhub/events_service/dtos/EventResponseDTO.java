package nl.eventhub.events_service.dtos;

import nl.eventhub.events_service.models.Event;

public class EventResponseDTO extends Event {
    private int availableTickets;

    public EventResponseDTO() {
        super();
    }

    public EventResponseDTO(Event event, int availableTickets) {
        super(event.getName(), event.getDescription(), event.getDateTime(), event.getLocation(), event.getTotalTicketCount());
        this.setId(event.getId());
        this.availableTickets = availableTickets;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }
}