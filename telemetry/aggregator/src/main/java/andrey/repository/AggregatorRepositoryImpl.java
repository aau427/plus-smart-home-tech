package andrey.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AggregatorRepositoryImpl implements AggregatorRepository {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    public void save(final SensorsSnapshotAvro snapshot) {
        log.info("Созраняю снэпшот для хаба {}", snapshot.getHubId());
        snapshots.put(snapshot.getHubId(), snapshot);
    }

    @Override
    public Optional<SensorsSnapshotAvro> findByHubId(final String hubId) {
        return Optional.ofNullable(snapshots.get(hubId));
    }
}
