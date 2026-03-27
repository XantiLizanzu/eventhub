package nl.eventhub.users_service.dtos;

public class OrganizerCreationDTO extends UserCreationDTO {
    private String name;

    public OrganizerCreationDTO() {}

    public OrganizerCreationDTO(String username, String password, String name) {
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
