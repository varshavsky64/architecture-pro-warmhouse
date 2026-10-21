package com.warmhouse.devices;

// «Адаптер протоколов партнёров»: translates commands and readings between the system format and device protocols
public interface ProtocolAdapter {

    void send(Device device, Command command);

    DeviceReading read(Device device);
}
