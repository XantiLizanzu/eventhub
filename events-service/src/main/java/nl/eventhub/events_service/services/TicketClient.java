package nl.eventhub.events_service.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TicketClient {

    private final RestTemplate restTemplate;
    
    @Value("${ticketing.service.url}")
    private String ticketingServiceUrl;

    public TicketClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getBookedTickets(Long eventId) {
        try {
            String url = ticketingServiceUrl + "availability/" + eventId;
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Failed to fetch booked tickets: " + e.getMessage());
            return 0;
        }
    }

    public int getAvailableTickets(Long eventId, int totalTicketCount) {
        int bookedTickets = getBookedTickets(eventId);
        return Math.max(totalTicketCount - bookedTickets, 0);
    }
}