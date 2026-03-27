package nl.eventhub.users_service.controllers;

import nl.eventhub.users_service.dtos.UserCreationDTO;
import nl.eventhub.users_service.models.User;
import nl.eventhub.users_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password");
    }

    @Test
    void registrate_shouldReturnUser() {
        when(userService.registrate(any(UserCreationDTO.class))).thenReturn(testUser);
        UserCreationDTO creationDTO = new UserCreationDTO(testUser.getUsername(), testUser.getPassword());
        ResponseEntity<User> result = userController.registrate(creationDTO);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testUser, result.getBody());
    }

    @Test
    void login_shouldReturnUserWhenSuccessful() {
        when(userService.login(anyString(), anyString())).thenReturn(Optional.of(testUser));
        ResponseEntity<User> result = userController.login("testuser", "password");
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testUser, result.getBody());
    }

    @Test
    void login_shouldReturnUnauthorizedWhenFailed() {
        when(userService.login(anyString(), anyString())).thenReturn(Optional.empty());
        ResponseEntity<User> result = userController.login("testuser", "wrongpassword");
        assertEquals(401, result.getStatusCode().value());
    }

    @Test
    void logout_shouldReturnOk() {
        doNothing().when(userService).logout(anyLong());
        ResponseEntity<Void> result = userController.logout(1L);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
}
