CREATE TABLE IF NOT EXISTS device_types (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    supported_commands TEXT[] NOT NULL
);

-- house_id has no foreign key: houses live in the database of the users service
CREATE TABLE IF NOT EXISTS devices (
    id UUID PRIMARY KEY,
    type_id UUID NOT NULL REFERENCES device_types (id),
    house_id UUID NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
