package nl.eventhub.users_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.users_service.dtos.OrganizerCreationDTO;
import nl.eventhub.users_service.models.Organizer;
import nl.eventhub.users_service.services.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Organizers")
@RequestMapping("/organizers")
public class OrganizerController {
    @Autowired
    private OrganizerService organizerService;

    @PostMapping("/")
    public ResponseEntity<Organizer> createOrganizer(@RequestBody OrganizerCreationDTO organizer) {
        return ResponseEntity.ok(organizerService.createOrganizer(organizer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable("id") Long id) {
        return organizerService.getOrganizerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organizer> updateOrganizer(@PathVariable("id") Long id, @RequestBody OrganizerCreationDTO updatedOrganizer) {
        try {
            return ResponseEntity.ok(organizerService.updateOrganizer(id, updatedOrganizer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Organizer>> getAllOrganizers() {
        return ResponseEntity.ok(organizerService.getAllOrganizers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable("id") Long id) {
        organizerService.deleteOrganizer(id);
        return ResponseEntity.ok().build();
    }
}
