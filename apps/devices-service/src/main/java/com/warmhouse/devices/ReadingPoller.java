package com.warmhouse.devices;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

// MVP: the emulator cannot push readings like a real device over MQTT, so the service polls it
@Component
public class ReadingPoller {

    private static final Logger log = LoggerFactory.getLogger(ReadingPoller.class);

    private final DeviceRepository devices;
    private final ProtocolAdapter protocolAdapter;
    private final DeviceEventPublisher events;
    private final List<UUID> typeIds;

    public ReadingPoller(DeviceRepository devices, ProtocolAdapter protocolAdapter, DeviceEventPublisher events,
                         @Value("${devices.readings.type-ids}") List<UUID> typeIds) {
        this.devices = devices;
        this.protocolAdapter = protocolAdapter;
        this.events = events;
        this.typeIds = typeIds;
    }

    @Scheduled(fixedDelayString = "${devices.readings.interval}")
    public void pollDevices() {
        for (var device : devices.findByTypeIdIn(typeIds)) {
            try {
                events.reading(protocolAdapter.read(device));
            } catch (RestClientException e) {
                log.warn("Device {} did not answer: {}", device.getSerialNumber(), e.getMessage());
            }
        }
    }
}
