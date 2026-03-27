package nl.eventhub.ticketing_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;


@RestController
@Tag(name = "Ticketing")
@RequestMapping("/events/{event_id}/tickets")
public class TicketController {

    @GetMapping("/events/{id}/availability")
    @Operation(summary = "Get an ticket availability")
    public ResponseEntity<Map<String, Integer>> getTicketAvailability(@PathVariable int eventId){
        int availableTickets = 10 + eventId * 7; //stub return some amount
        Map<String, Integer> response = new HashMap<>();
        response.put("available", availableTickets);

        return ResponseEntity.ok(response);
    }
}