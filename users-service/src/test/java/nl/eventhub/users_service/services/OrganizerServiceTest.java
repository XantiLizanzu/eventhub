package nl.eventhub.users_service.services;

import nl.eventhub.users_service.dtos.OrganizerCreationDTO;
import nl.eventhub.users_service.models.Organizer;
import nl.eventhub.users_service.repositories.OrganizerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizerServiceTest {
    @Mock
    private OrganizerRepository organizerRepository;

    @InjectMocks
    private OrganizerService organizerService;

    private Organizer testOrganizer;

    @BeforeEach
    void setUp() {
        testOrganizer = new Organizer("org_user", "pass", "Org Name");
    }

    @Test
    void createOrganizer_shouldSaveOrganizer() {
        when(organizerRepository.save(any(Organizer.class))).thenReturn(testOrganizer);
        OrganizerCreationDTO creationDTO = new OrganizerCreationDTO(testOrganizer.getUsername(), testOrganizer.getPassword(), testOrganizer.getName());
        Organizer result = organizerService.createOrganizer(creationDTO);
        assertNotNull(result);
        assertEquals("Org Name", result.getName());
        verify(organizerRepository, times(1)).save(any(Organizer.class));
    }

    @Test
    void getOrganizerById_shouldReturnOrganizerWhenFound() {
        when(organizerRepository.findById(anyLong())).thenReturn(Optional.of(testOrganizer));
        Optional<Organizer> result = organizerService.getOrganizerById(1L);
        assertTrue(result.isPresent());
        assertEquals(testOrganizer, result.get());
    }

    @Test
    void getAllOrganizers_shouldReturnList() {
        when(organizerRepository.findAll()).thenReturn(Collections.singletonList(testOrganizer));
        List<Organizer> result = organizerService.getAllOrganizers();
        assertEquals(1, result.size());
    }

    @Test
    void updateOrganizer_shouldUpdateFields() {
        when(organizerRepository.findById(anyLong())).thenReturn(Optional.of(testOrganizer));
        when(organizerRepository.save(any(Organizer.class))).thenAnswer(i -> i.getArguments()[0]);

        OrganizerCreationDTO updatedData = new OrganizerCreationDTO("new_user", "new_pass", "New Name");
        Organizer result = organizerService.updateOrganizer(1L, updatedData);

        assertEquals("New Name", result.getName());
        assertEquals("new_user", result.getUsername());
        verify(organizerRepository, times(1)).save(any(Organizer.class));
    }

    @Test
    void deleteOrganizer_shouldCallRepository() {
        doNothing().when(organizerRepository).deleteById(anyLong());
        organizerService.deleteOrganizer(1L);
        verify(organizerRepository, times(1)).deleteById(1L);
    }
}
