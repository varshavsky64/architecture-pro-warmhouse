package com.warmhouse.devices;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// MVP: partner devices are emulated by temperature-api; the target system talks to devices over MQTT
@Component
public class EmulatorProtocolAdapter implements ProtocolAdapter {

    private static final Logger log = LoggerFactory.getLogger(EmulatorProtocolAdapter.class);

    private final RestClient restClient;

    public EmulatorProtocolAdapter(RestClient.Builder restClientBuilder, @Value("${devices.emulator.url}") String emulatorUrl) {
        this.restClient = restClientBuilder.baseUrl(emulatorUrl).build();
    }

    @Override
    public void send(Device device, Command command) {
        // The emulator cannot take commands, so the command only goes to the log
        log.info("Command {} with params {} sent to device {}", command.action(), command.params(), device.getSerialNumber());
    }

    @Override
    public DeviceReading read(Device device) {
        var response = restClient.get()
                .uri("/temperature/{sensorId}", device.getSerialNumber())
                .retrieve()
                .body(EmulatorReading.class);
        return new DeviceReading(device.getId(), response.value(), response.timestamp());
    }

    // Part of the temperature-api response that the adapter needs
    record EmulatorReading(double value, Instant timestamp) {
    }
}
