package com.warmhouse.devices;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// «API устройств», see api/devices-openapi.yaml
@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final ConnectionService connectionService;
    private final CommandService commandService;
    private final DeviceRepository devices;

    public DeviceController(ConnectionService connectionService, CommandService commandService, DeviceRepository devices) {
        this.connectionService = connectionService;
        this.commandService = commandService;
        this.devices = devices;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse connect(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody ConnectDeviceRequest request) {
        return DeviceResponse.from(connectionService.connect(userId, request));
    }

    @GetMapping("/{id}")
    public DeviceResponse getDevice(@PathVariable UUID id) {
        return devices.findById(id).map(DeviceResponse::from).orElseThrow(() -> ApiException.deviceNotFound(id));
    }

    @PostMapping("/{id}/commands")
    public DeviceState sendCommand(@PathVariable UUID id, @Valid @RequestBody Command command) {
        return commandService.execute(id, command);
    }
}
