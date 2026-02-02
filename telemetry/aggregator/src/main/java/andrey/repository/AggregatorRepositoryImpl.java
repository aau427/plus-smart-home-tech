package andrey.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AggregatorRepositoryImpl implements AggregatorRepository {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    public void save(final SensorsSnapshotAvro snapshot) {
        snapshots.put(snapshot.getHubId(), snapshot);
    }

    @Override
    public Optional<SensorsSnapshotAvro> findByHubId(final String hubId) {
        return Optional.ofNullable(snapshots.get(hubId));
    }
}
