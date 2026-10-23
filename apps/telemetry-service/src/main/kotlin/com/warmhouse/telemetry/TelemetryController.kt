package com.warmhouse.telemetry

import java.time.Instant
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// «API показаний», see api/telemetry-openapi.yaml
@RestController
@RequestMapping("/telemetry")
class TelemetryController(
    private val repository: TelemetryRepository,
    private val registry: DeviceRegistry,
) {

    @GetMapping
    fun readings(
        @RequestParam deviceId: UUID,
        @RequestParam from: Instant,
        @RequestParam to: Instant,
    ): List<TelemetryData> {
        if (from.isAfter(to)) {
            throw ApiException(HttpStatus.BAD_REQUEST, "INVALID_PERIOD", "Начало периода позже конца")
        }
        if (!registry.isKnown(deviceId)) {
            throw ApiException(HttpStatus.NOT_FOUND, "DEVICE_NOT_FOUND", "Устройство $deviceId не найдено")
        }
        return repository.findByDevice(deviceId, from, to)
    }
}
