package nl.eventhub.ticketing_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Reserve a ticket")
    public ResponseEntity<Ticket> reserveTicket(@PathVariable long eventId, @RequestParam long userId) {
        return ResponseEntity.ok(ticketingService.reserveTicket(eventId, userId));
    }

    @PostMapping("/tickets/{ticketId}/unreserve")
    @Operation(summary = "Unreserve a ticket")
    public ResponseEntity<Ticket> unreserveTicket(@PathVariable long ticketId) {
        return ticketingService.unreserveTicket(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tickets/{ticketId}/complete")
    @Operation(summary = "Complete the reservation of a ticket")
    public ResponseEntity<Ticket> completeTicket(@PathVariable long ticketId) {
        return ticketingService.completeTicket(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/availability/{eventId}")
    @Operation(summary = "Get the amount of tickets which are reserved")
    public ResponseEntity<Integer> getAvailability(@PathVariable long eventId) {
        return ResponseEntity.ok(ticketingService.getAvailability(eventId));
    }

    @GetMapping("/events/{eventId}/tickets/{ticketId}")
    @Operation(summary = "Get a ticket by event and its ID")
    public ResponseEntity<Ticket> getTicket(@PathVariable long eventId, @PathVariable long ticketId) {
        return ticketingService.getTicket(eventId, ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/")
    @Operation(summary = "Get all tickets for a user")
    public ResponseEntity<List<Ticket>> getTicketsForUser(@PathVariable long userId) {
        return ResponseEntity.ok(ticketingService.getTicketsForUser(userId));
    }
}
