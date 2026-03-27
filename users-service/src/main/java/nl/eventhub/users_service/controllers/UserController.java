package nl.eventhub.users_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.users_service.dtos.UserCreationDTO;
import nl.eventhub.users_service.models.User;
import nl.eventhub.users_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registrate")
    public ResponseEntity<User> registrate(@RequestBody UserCreationDTO user) {
        return ResponseEntity.ok(userService.registrate(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok().build();
    }
}
