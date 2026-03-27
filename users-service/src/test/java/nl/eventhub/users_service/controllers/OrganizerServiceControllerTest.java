package nl.eventhub.users_service.controllers;

import nl.eventhub.users_service.dtos.OrganizerCreationDTO;
import nl.eventhub.users_service.models.Organizer;
import nl.eventhub.users_service.services.OrganizerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizerServiceControllerTest {
    @Mock
    private OrganizerService organizerService;

    @InjectMocks
    private OrganizerController organizerController;

    private Organizer testOrganizer;

    @BeforeEach
    void setUp() {
        testOrganizer = new Organizer("org_user", "pass", "Org Name");
    }

    @Test
    void createOrganizer_shouldReturnCreated() {
        when(organizerService.createOrganizer(any(OrganizerCreationDTO.class))).thenReturn(testOrganizer);
        OrganizerCreationDTO creationDTO = new OrganizerCreationDTO(testOrganizer.getUsername(), testOrganizer.getPassword(), testOrganizer.getName());
        ResponseEntity<Organizer> result = organizerController.createOrganizer(creationDTO);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testOrganizer, result.getBody());
    }

    @Test
    void getOrganizerById_shouldReturnOrganizerWhenFound() {
        when(organizerService.getOrganizerById(anyLong())).thenReturn(Optional.of(testOrganizer));
        ResponseEntity<Organizer> result = organizerController.getOrganizerById(1L);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testOrganizer, result.getBody());
    }

    @Test
    void getOrganizerById_shouldReturnNotFoundWhenMissing() {
        when(organizerService.getOrganizerById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Organizer> result = organizerController.getOrganizerById(1L);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void updateOrganizer_shouldReturnUpdated() {
        when(organizerService.updateOrganizer(anyLong(), any(OrganizerCreationDTO.class))).thenReturn(testOrganizer);
        OrganizerCreationDTO creationDTO = new OrganizerCreationDTO(testOrganizer.getUsername(), testOrganizer.getPassword(), testOrganizer.getName());
        ResponseEntity<Organizer> result = organizerController.updateOrganizer(1L, creationDTO);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testOrganizer, result.getBody());
    }

    @Test
    void updateOrganizer_shouldReturnNotFoundOnException() {
        when(organizerService.updateOrganizer(anyLong(), any(OrganizerCreationDTO.class))).thenThrow(new RuntimeException());
        OrganizerCreationDTO creationDTO = new OrganizerCreationDTO(testOrganizer.getUsername(), testOrganizer.getPassword(), testOrganizer.getName());
        ResponseEntity<Organizer> result = organizerController.updateOrganizer(1L, creationDTO);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void getAllOrganizers_shouldReturnList() {
        when(organizerService.getAllOrganizers()).thenReturn(Collections.singletonList(testOrganizer));
        ResponseEntity<List<Organizer>> result = organizerController.getAllOrganizers();
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void deleteOrganizer_shouldReturnOk() {
        doNothing().when(organizerService).deleteOrganizer(anyLong());
        ResponseEntity<Void> result = organizerController.deleteOrganizer(1L);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
}
