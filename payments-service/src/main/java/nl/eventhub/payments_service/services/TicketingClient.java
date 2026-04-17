package nl.eventhub.payments_service.services;

import nl.eventhub.payments_service.models.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TicketingClient {
    private final RestTemplate restTemplate;

    @Value("${ticketing.service.url}")
    private String ticketingServiceUrl;

    public TicketingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void completeTicket(Long ticketId) {
        String url = ticketingServiceUrl + "tickets/" + ticketId + "/complete";
        restTemplate.postForEntity(url, null, Ticket.class);
    }
}
