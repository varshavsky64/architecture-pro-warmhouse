package com.warmhouse.devices;

import java.util.UUID;

public record DeviceResponse(UUID id, UUID typeId, UUID houseId, String serialNumber, String status) {

    static DeviceResponse from(Device device) {
        return new DeviceResponse(device.getId(), device.getTypeId(), device.getHouseId(),
                device.getSerialNumber(), device.getState().status());
    }
}
