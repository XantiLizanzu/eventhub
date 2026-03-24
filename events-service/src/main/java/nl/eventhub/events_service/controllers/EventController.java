package nl.eventhub.events_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.events_service.models.Event;
import nl.eventhub.events_service.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Events")
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    private EventService eventService;

    public record EventCreationDTO(String name, String description, LocalDateTime dateTime, String location, int totalTicketCount) {}

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

    @PostMapping
    @Operation(summary = "Create a new event")
    public Event createEvent(@RequestBody EventCreationDTO request) {
        Event event = new Event(request.name(), request.description(), request.dateTime(), request.location(), request.totalTicketCount());
        return eventService.createEvent(event);
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