package com.warmhouse.temperature;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperature")
public class TemperatureController {

    private static final double MIN_TEMPERATURE = 18.0;
    private static final double MAX_TEMPERATURE = 28.0;

    // GET /temperature?location=Kitchen — the monolith asks by room
    @GetMapping
    public TemperatureResponse byLocation(@RequestParam(required = false) String location,
                                          @RequestParam(required = false) String sensorId) {
        return randomReading(location, sensorId);
    }

    // GET /temperature/1 — the monolith asks by sensor ID
    @GetMapping("/{sensorId}")
    public TemperatureResponse bySensorId(@PathVariable String sensorId,
                                          @RequestParam(required = false) String location) {
        return randomReading(location, sensorId);
    }

    private TemperatureResponse randomReading(String location, String sensorId) {
        // If no location is provided, use a default based on sensor ID
        var room = StringUtils.hasText(location) ? location : locationBySensorId(sensorId);
        // If no sensor ID is provided, generate one based on location
        var id = StringUtils.hasText(sensorId) ? sensorId : sensorIdByLocation(room);

        var value = ThreadLocalRandom.current().nextDouble(MIN_TEMPERATURE, MAX_TEMPERATURE);

        return new TemperatureResponse(
                Math.round(value * 100) / 100.0,
                "°C",
                Instant.now().truncatedTo(ChronoUnit.MILLIS),
                room,
                "active",
                id,
                "temperature",
                "Temperature reading from " + room);
    }

    private static String locationBySensorId(String sensorId) {
        return switch (sensorId) {
            case "1" -> "Living Room";
            case "2" -> "Bedroom";
            case "3" -> "Kitchen";
            case null, default -> "Unknown";
        };
    }

    private static String sensorIdByLocation(String location) {
        return switch (location) {
            case "Living Room" -> "1";
            case "Bedroom" -> "2";
            case "Kitchen" -> "3";
            default -> "0";
        };
    }
}
