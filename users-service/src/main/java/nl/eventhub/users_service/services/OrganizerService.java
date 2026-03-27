package nl.eventhub.users_service.services;

import nl.eventhub.users_service.dtos.OrganizerCreationDTO;
import nl.eventhub.users_service.models.Organizer;
import nl.eventhub.users_service.repositories.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    public Organizer createOrganizer(OrganizerCreationDTO creationDTO) {
        Organizer organizer = new Organizer(creationDTO.getUsername(), creationDTO.getPassword(), creationDTO.getName());
        return organizerRepository.save(organizer);
    }

    public Optional<Organizer> getOrganizerById(Long id) {
        return organizerRepository.findById(id);
    }

    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    public Organizer updateOrganizer(Long id, OrganizerCreationDTO creationDTO) {
        return organizerRepository.findById(id)
                .map(organizer -> {
                    organizer.setName(creationDTO.getName());
                    organizer.setUsername(creationDTO.getUsername());
                    organizer.setPassword(creationDTO.getPassword());
                    return organizerRepository.save(organizer);
                }).orElseThrow(() -> new RuntimeException("Organizer not found"));
    }

    public void deleteOrganizer(Long id) {
        organizerRepository.deleteById(id);
    }
}
