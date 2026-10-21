package com.warmhouse.devices;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import tools.jackson.databind.json.JsonMapper;

// «Kafka-клиент»: publishes device events and readings for the telemetry service
@Component
public class DeviceEventPublisher {

    static final String EVENTS_TOPIC = "devices.events";
    static final String READINGS_TOPIC = "devices.readings";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public DeviceEventPublisher(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void deviceAdded(Device device) {
        send(EVENTS_TOPIC, device.getId(), new DeviceEvent("DEVICE_ADDED", device.getId(), device.getTypeId(),
                device.getHouseId(), device.getSerialNumber()));
    }

    public void reading(DeviceReading reading) {
        send(READINGS_TOPIC, reading.deviceId(), reading);
    }

    // The device ID is the message key, so the messages of one device stay in order
    private void send(String topic, UUID deviceId, Object message) {
        kafkaTemplate.send(topic, deviceId.toString(), jsonMapper.writeValueAsString(message));
    }
}
