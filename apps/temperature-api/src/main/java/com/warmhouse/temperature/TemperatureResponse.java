package com.warmhouse.temperature;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

// Field names match TemperatureResponse in the monolith: apps/smart_home/services/temperature_service.go
public record TemperatureResponse(
        double value,
        String unit,
        Instant timestamp,
        String location,
        String status,
        @JsonProperty("sensor_id") String sensorId,
        @JsonProperty("sensor_type") String sensorType,
        String description) {
}
