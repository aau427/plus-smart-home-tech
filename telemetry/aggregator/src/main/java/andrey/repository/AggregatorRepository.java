package andrey.repository;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregatorRepository {
    void save(SensorsSnapshotAvro snapshot);

    Optional<SensorsSnapshotAvro> findByHubId(String hubId);
}
