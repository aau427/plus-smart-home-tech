package andrey.service;

import andrey.dto.hub.HubEvent;
import andrey.dto.sensor.SensorEvent;

public interface CollectorService {
    void handleHubEvent(HubEvent hubEvent);

    void handleSensorEvent(SensorEvent sensorEvent);
}
