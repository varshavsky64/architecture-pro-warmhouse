package com.warmhouse.devices;

import java.time.Instant;

import jakarta.persistence.Embeddable;

// Value object: the current state of a device, stored in the devices table
@Embeddable
public record DeviceState(String status, Instant updatedAt) {
}
