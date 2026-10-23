package com.warmhouse.devices;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;

// Value object: a command for a device, for example TURN_ON or SET_TEMPERATURE with params {"value": "22"}
public record Command(@NotBlank String action, Map<String, String> params) {
}
