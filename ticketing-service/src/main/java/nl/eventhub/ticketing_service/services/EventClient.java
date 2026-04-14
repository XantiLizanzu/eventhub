package nl.eventhub.ticketing_service.services;

import nl.eventhub.ticketing_service.models.EventResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventClient {
    private final RestTemplate restTemplate;

    @Value("${events.service.url}")
    private String eventsServiceUrl;

    public EventClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<EventResponseDTO> getEventResponse(Long ticketId) {
        String url = eventsServiceUrl + "events/" + ticketId;
        return restTemplate.getForEntity(url, EventResponseDTO.class);
    }
}
