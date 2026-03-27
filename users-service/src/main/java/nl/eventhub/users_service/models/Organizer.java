package nl.eventhub.users_service.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("organizer")
public class Organizer extends User{
    private String name;

    public Organizer() {
    }

    public Organizer(String username, String password, String name) {
        super(username, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
