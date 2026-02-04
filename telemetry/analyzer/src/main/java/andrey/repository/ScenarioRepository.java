package andrey.repository;

import andrey.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {


    Optional<Scenario> findByHubIdAndName(String hubId, String name);

    List<Scenario> findAllByHubId(String hubId);

    void deleteByHubIdAndName(String hubId, String name);
}
