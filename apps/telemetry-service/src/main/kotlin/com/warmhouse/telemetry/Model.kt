package com.warmhouse.telemetry

import java.time.Instant
import java.util.UUID

// A stored reading; it is also the payload of «новое показание», see api/telemetry-asyncapi.yaml
data class TelemetryData(val id: UUID, val deviceId: UUID, val value: Double, val recordedAt: Instant)

// Event from the devices service, for example «устройство добавлено» with type DEVICE_ADDED
data class DeviceEvent(val type: String, val deviceId: UUID, val typeId: UUID, val houseId: UUID, val serialNumber: String)

// A reading from the devices service in the system format
data class DeviceReading(val deviceId: UUID, val value: Double, val recordedAt: Instant)

data class ErrorResponse(val code: String, val message: String)
