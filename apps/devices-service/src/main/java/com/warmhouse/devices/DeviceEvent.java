package com.warmhouse.devices;

import java.util.UUID;

// Event published to devices.events, for example «устройство добавлено» with type DEVICE_ADDED
public record DeviceEvent(String type, UUID deviceId, UUID typeId, UUID houseId, String serialNumber) {
}
