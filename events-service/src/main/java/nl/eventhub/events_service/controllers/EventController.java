package nl.eventhub.events_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.events_service.dtos.EventCreationDTO;
import nl.eventhub.events_service.models.Event;
import nl.eventhub.events_service.services.EventService;
import nl.eventhub.events_service.services.TicketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "Events")
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    private EventService eventService;

    @Autowired
    private TicketClient ticketClient;

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieves a list of all available events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an event")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{eventId}/availability")
    @Operation(summary="Get the amount of available tickets")
    public ResponseEntity<Map<String, Object>> getEventAvailability(@PathVariable Long eventId){
        int availableTickets = ticketClient.getAvailableTickets(eventId);
        Map<String, Object> response = new HashMap<>();
        response.put("eventId", eventId);
        response.put("availableTickets", availableTickets);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(summary = "Create a new event")
    public Event createEvent(@RequestBody EventCreationDTO request) {
        return eventService.createEvent(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event", description = "Updates an existing event and puts a message in the event updates queue for the notification service.")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event", description = "Deletes a specific event by its unique identifier")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}