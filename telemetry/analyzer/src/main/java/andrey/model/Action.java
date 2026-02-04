package andrey.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

@Entity
@Table(name = "actions")
@SecondaryTable(
        name = "scenario_actions",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "action_id") // связь с scenario_actions.action_id
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ActionTypeAvro type;

    @Column(name = "value") // из таблицы actions
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "scenario_id", table = "scenario_actions")
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "sensor_id", table = "scenario_actions") // колонка в scenario_actions
    private Sensor sensor;
}
