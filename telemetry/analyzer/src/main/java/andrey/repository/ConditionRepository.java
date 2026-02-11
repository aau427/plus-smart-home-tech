package andrey.repository;

import andrey.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {


    List<Condition> findAllBySensorId(String sensorId);

    List<Condition> findAllByScenarioId(Long scenarioId);

    @Modifying
    @Query("delete from Condition c where c.scenario.id = :scenarioId")
    void deleteAllByScenarioId(Long scenarioId);
}
