package com.warmhouse.devices;

import java.time.Instant;
import java.util.UUID;

// A device reading in the system format, published to devices.readings
public record DeviceReading(UUID deviceId, double value, Instant recordedAt) {
}
