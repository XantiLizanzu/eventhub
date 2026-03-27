package nl.eventhub.users_service.dtos;

public class UserCreationDTO {
    private String username;
    private String password;

    public UserCreationDTO() {}

    public UserCreationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
