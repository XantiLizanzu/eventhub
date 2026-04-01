package nl.eventhub.notifications_service.repositories;

import nl.eventhub.notifications_service.models.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, Long> {
    EventSubscription findByUserIdAndEventId(Long userId, Long eventId);
    List<EventSubscription> findByEventId(Long eventId);
}