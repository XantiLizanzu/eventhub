package nl.eventhub.ticketing_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.ticketing_service.models.Ticket;
import nl.eventhub.ticketing_service.services.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Ticketing")
@RequestMapping("/ticketing")
public class TicketingController {

    private final TicketingService ticketingService;

    @Autowired
    public TicketingController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @PostMapping("/events/{eventId}/reserve")
    public ResponseEntity<Ticket> reserveTicket(@PathVariable long eventId, @RequestParam long userId) {
        return ResponseEntity.ok(ticketingService.reserveTicket(eventId, userId));
    }

    @PostMapping("/tickets/{ticketId}/unreserve")
    public ResponseEntity<Ticket> unreserveTicket(@PathVariable long ticketId) {
        return ticketingService.unreserveTicket(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tickets/{ticketId}/complete")
    public ResponseEntity<Ticket> completeTicket(@PathVariable long ticketId) {
        return ticketingService.completeTicket(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/availability/{eventId}")
    public ResponseEntity<Integer> getAvailability(@PathVariable long eventId) {
        return ResponseEntity.ok(ticketingService.getAvailability(eventId));
    }

    @GetMapping("/events/{eventId}/tickets/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable long eventId, @PathVariable long ticketId) {
        return ticketingService.getTicket(eventId, ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/")
    public ResponseEntity<List<Ticket>> getTicketsForUser(@PathVariable long userId) {
        return ResponseEntity.ok(ticketingService.getTicketsForUser(userId));
    }
}
