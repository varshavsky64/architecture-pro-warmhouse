-- A new device type is a new row here, not new code
INSERT INTO device_types (id, name, supported_commands) VALUES
    ('5c2b8e1a-3f4d-4b6c-9a7e-0d1f2e3a4b51', 'Датчик температуры', '{}'),
    ('9a7e5c3b-1d2f-4e6a-8b0c-2d4f6a8c0e11', 'Термостат', '{TURN_ON,TURN_OFF,SET_TEMPERATURE}'),
    ('6d3c9f2b-4a5e-4c7d-8b8f-1e2a3b4c5d62', 'Лампа', '{TURN_ON,TURN_OFF}'),
    ('7e4d0a3c-5b6f-4d8e-9c9a-2f3b4c5d6e73', 'Ворота', '{OPEN,CLOSE}')
ON CONFLICT (id) DO NOTHING;
