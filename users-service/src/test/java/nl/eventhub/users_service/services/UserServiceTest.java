package nl.eventhub.users_service.services;

import nl.eventhub.users_service.dtos.UserCreationDTO;
import nl.eventhub.users_service.models.User;
import nl.eventhub.users_service.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password");
    }

    @Test
    void registrate_shouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserCreationDTO creationDTO = new UserCreationDTO(testUser.getUsername(), testUser.getPassword());
        User result = userService.registrate(creationDTO);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registrate_should_not_have_id_before_saving() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserCreationDTO creationDTO = new UserCreationDTO("newuser", "pass");
        
        User result = userService.registrate(creationDTO);
        
        assertNull(result.getId());
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_shouldReturnUserWhenCredentialsMatch() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));
        Optional<User> result = userService.login("testuser", "password");
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void login_shouldReturnEmptyWhenCredentialsDoNotMatch() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));
        Optional<User> result = userService.login("testuser", "wrongpassword");
        assertFalse(result.isPresent());
    }

    @Test
    void logout_shouldDoNothing() {
        userService.logout(1L);
    }
}
