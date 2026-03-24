package nl.eventhub.notifications_service.models;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private int totalTicketCount;

    // Constructors
    public Event() {
    }

    public Event(Long id, String name, String description, LocalDateTime dateTime, String location, int totalTicketCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.totalTicketCount = totalTicketCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                '}';
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public void setTotalTicketCount(int totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }
}