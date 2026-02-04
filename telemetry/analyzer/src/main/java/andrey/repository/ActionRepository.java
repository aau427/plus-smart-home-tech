package andrey.repository;

import andrey.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    List<Action> findAllByScenarioId(Long scenarioId);

    @Modifying
    @Query("delete from Action a where a.scenario.id = :scenarioId")
    void deleteAllByScenarioId(Long scenarioId);
}
