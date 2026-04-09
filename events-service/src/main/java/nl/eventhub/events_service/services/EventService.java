package nl.eventhub.events_service.services;

import jakarta.annotation.PostConstruct;
import nl.eventhub.events_service.models.Event;
import nl.eventhub.events_service.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventMessageSender eventMessageSender;

    @PostConstruct
    public void initializeSampleEvents() {

        if (eventRepository.count() == 0) {
            eventRepository.saveAll(List.of(
                    new Event("Enschede Marathon", "The Enschede Marathon is the oldest marathon in" +
                            " the Netherlands and Western Europe. Today, nearly 11,000 participants take part, and" +
                            " the number continues to grow.",
                            LocalDateTime.of(2026, 4, 12, 9, 30),
                            "H.J. van Heekplein", 10800),
                    new Event("The colourful universe of Bas Kosters", "Through textiles, drawings," +
                            " graphic art and ceramics, he tells personal stories about loneliness, happiness, " +
                            "freedom, sexuality and identity.",
                            LocalDateTime.of(2026, 4, 14, 19, 30),
                            "Rijksmuseum Twenthe", 100),
                    new Event("Pandora", "Pandora is a week-long puzzle hunt on the Campus of the " +
                            "University of Twente. Be careful for other players, because you don't want to be killed!",
                            LocalDateTime.of(2026, 4, 20, 19, 0),
                            "Campus University of Twente", 300)
                )
            );
        }
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setDateTime(eventDetails.getDateTime());
        event.setLocation(eventDetails.getLocation());

        Event updatedEvent = eventRepository.save(event);
        
        // Send message to RabbitMQ
        eventMessageSender.sendEventUpdatedMessage(updatedEvent);
        
        return updatedEvent;
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
    }
}