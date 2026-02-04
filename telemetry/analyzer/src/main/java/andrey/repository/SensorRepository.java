package andrey.repository;

import andrey.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    long deleteByIdAndHubId(String id, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    int countByIdInAndHubId(Set<String> ids, String hubId);
}
