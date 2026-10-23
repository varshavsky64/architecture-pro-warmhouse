package com.warmhouse.telemetry

import java.util.UUID
import org.springframework.stereotype.Service

// «Справочник устройств»: a copy of the device list, built from events of the devices service
@Service
class DeviceRegistry(private val repository: TelemetryRepository) {

    fun onDeviceEvent(event: DeviceEvent) {
        if (event.type == "DEVICE_ADDED") {
            repository.saveDevice(event)
        }
    }

    fun isKnown(deviceId: UUID): Boolean = repository.deviceExists(deviceId)
}
