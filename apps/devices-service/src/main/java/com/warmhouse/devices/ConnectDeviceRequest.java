package com.warmhouse.devices;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConnectDeviceRequest(@NotNull UUID houseId, @NotNull UUID typeId, @NotBlank String serialNumber) {
}
