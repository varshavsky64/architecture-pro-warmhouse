package com.warmhouse.devices;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

// «Команды»: checks that the device type supports the command, sends it and keeps the device state
@Service
public class CommandService {

    private final DeviceRepository devices;
    private final DeviceTypeRepository deviceTypes;
    private final ProtocolAdapter protocolAdapter;

    public CommandService(DeviceRepository devices, DeviceTypeRepository deviceTypes, ProtocolAdapter protocolAdapter) {
        this.devices = devices;
        this.deviceTypes = deviceTypes;
        this.protocolAdapter = protocolAdapter;
    }

    public DeviceState execute(UUID deviceId, Command command) {
        var device = devices.findById(deviceId).orElseThrow(() -> ApiException.deviceNotFound(deviceId));
        var type = deviceTypes.findById(device.getTypeId()).orElseThrow();

        if (!type.supports(command.action())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "COMMAND_NOT_SUPPORTED",
                    "Тип устройства «" + type.getName() + "» не поддерживает команду " + command.action());
        }

        protocolAdapter.send(device, command);
        var state = device.apply(command);
        devices.save(device);
        return state;
    }
}
