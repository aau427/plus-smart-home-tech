package andrey.service;

import andrey.repository.AggregatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AggregatorServiceImpl implements AggregatorService {
    private final AggregatorRepository repository;

    @Override
    public Optional<SensorsSnapshotAvro> updateState(final SensorEventAvro event) {
        if (event == null || event.getHubId() == null || event.getId() == null) {
            log.warn("Получил кривое события датчика: {}", event);
            return Optional.empty();
        }

        SensorsSnapshotAvro snapshotAvro;

        Optional<SensorsSnapshotAvro> optCurrentSnapshot = repository.findByHubId(event.getHubId());
        if (optCurrentSnapshot.isPresent()) {
            if(!needsUpdate(optCurrentSnapshot.get(), event)) {
                return Optional.empty();
            }
            snapshotAvro = updateSnapshot(optCurrentSnapshot.get(), event);
        } else {
            snapshotAvro = createNewSnapshot(event);
        }
        repository.save(snapshotAvro);
        return Optional.of(snapshotAvro);
    }

    private boolean needsUpdate(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        SensorStateAvro oldEvent = snapshot.getSensorsState().get(event.getId());

        if (oldEvent == null) {
            return true;
        }
        if (event.getTimestamp().isBefore(oldEvent.getTimestamp())) {
            return false;
        }
        return !oldEvent.getData().equals(event.getPayload());
    }

    private SensorsSnapshotAvro updateSnapshot(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        Map<String, SensorStateAvro> newStates = new HashMap<>(snapshot.getSensorsState());

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        newStates.put(event.getId(), newState);

        Instant newTimestamp = snapshot.getTimestamp().isAfter(event.getTimestamp())
                ? snapshot.getTimestamp()
                : event.getTimestamp();

        return SensorsSnapshotAvro.newBuilder(snapshot)
                .setTimestamp(newTimestamp)
                .setSensorsState(newStates)
                .build();
    }

    private SensorsSnapshotAvro createNewSnapshot(SensorEventAvro event) {
        log.info("Создаю новый снэпшот для хаба = {}", event.getHubId());

        Map<String, SensorStateAvro> initialState = new HashMap<>();
        initialState.put(event.getId(), SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build());

        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(initialState)
                .build();
    }
}
