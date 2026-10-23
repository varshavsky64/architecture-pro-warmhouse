CREATE EXTENSION IF NOT EXISTS timescaledb;

-- Copy of the device list, built from devices.events
CREATE TABLE IF NOT EXISTS known_devices (
    device_id UUID PRIMARY KEY,
    type_id UUID NOT NULL,
    house_id UUID NOT NULL
);

-- A hypertable needs the time column in every unique key, so it is part of the primary key
CREATE TABLE IF NOT EXISTS telemetry_data (
    id UUID NOT NULL,
    device_id UUID NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id, recorded_at)
);

SELECT create_hypertable('telemetry_data', by_range('recorded_at'), if_not_exists => TRUE);

CREATE INDEX IF NOT EXISTS telemetry_data_device_time ON telemetry_data (device_id, recorded_at);
