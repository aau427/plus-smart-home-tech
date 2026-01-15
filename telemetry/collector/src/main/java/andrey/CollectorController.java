package andrey;

import andrey.dto.hub.HubEvent;
import andrey.dto.sensor.SensorEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import andrey.service.CollectorService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {
    private CollectorService collectorService;

    @PostMapping("/sensors")
    public void handleSensorEvent(@Valid @RequestBody SensorEvent event) {
        collectorService.handleSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void handleHubEvent(@Valid @RequestBody HubEvent event) {
        collectorService.handleHubEvent(event);
    }

}
