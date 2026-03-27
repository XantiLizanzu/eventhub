package nl.eventhub.users_service.services;

import nl.eventhub.users_service.dtos.UserCreationDTO;
import nl.eventhub.users_service.models.User;
import nl.eventhub.users_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registrate(UserCreationDTO creationDTO) {
        User user = new User(creationDTO.getUsername(), creationDTO.getPassword());
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        // Dummy implementation for now: find user by username and check password
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public void logout(Long userId) {
        // Dummy implementation: nothing to do
    }
}
