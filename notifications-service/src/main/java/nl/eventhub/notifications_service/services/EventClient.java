package nl.eventhub.notifications_service.services;

import nl.eventhub.notifications_service.models.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventClient {

    private final RestTemplate restTemplate;
    private final String eventsServiceUrl;

    public EventClient(RestTemplate restTemplate, 
                      @Value("${EVENTS_SERVICE_HOST:events-service}") String eventsServiceHost,
                      @Value("${EVENTS_SERVICE_PORT:8081}") String eventsServicePort) {
        this.restTemplate = restTemplate;
        this.eventsServiceUrl = "http://" + eventsServiceHost + ":" + eventsServicePort;
    }

    public boolean eventExists(Long eventId) {
        try {
            String url = eventsServiceUrl + "/" + eventId;
            Event event = restTemplate.getForObject(url, Event.class);
            return event != null;
        } catch (Exception e) {
            // Event not found or service unavailable
            return false;
        }
    }
}