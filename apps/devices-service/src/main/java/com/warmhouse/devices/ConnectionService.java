package com.warmhouse.devices;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

// «Подключение устройств»: the user connects a device to a house without a specialist visit
@Service
public class ConnectionService {

    private final UsersClient usersClient;
    private final DeviceTypeRepository deviceTypes;
    private final DeviceRepository devices;
    private final DeviceEventPublisher events;

    public ConnectionService(UsersClient usersClient, DeviceTypeRepository deviceTypes, DeviceRepository devices,
                             DeviceEventPublisher events) {
        this.usersClient = usersClient;
        this.deviceTypes = deviceTypes;
        this.devices = devices;
        this.events = events;
    }

    public Device connect(UUID userId, ConnectDeviceRequest request) {
        usersClient.checkHouse(userId, request.houseId());

        if (!deviceTypes.existsById(request.typeId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "UNKNOWN_DEVICE_TYPE",
                    "Тип устройства " + request.typeId() + " не найден");
        }
        if (devices.existsBySerialNumber(request.serialNumber())) {
            throw new ApiException(HttpStatus.CONFLICT, "DEVICE_ALREADY_CONNECTED",
                    "Устройство с серийным номером " + request.serialNumber() + " уже подключено");
        }

        var device = devices.save(new Device(UUID.randomUUID(), request.typeId(), request.houseId(),
                request.serialNumber(), new DeviceState("OFF", Instant.now())));
        events.deviceAdded(device);
        return device;
    }
}
