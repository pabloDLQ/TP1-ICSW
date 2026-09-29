INSERT INTO tipo_habitacion (id, capacidad, costo_por_noche, descripcion) VALUES
(1, 1, 50800, 'Individual Estándar: habitación para una persona.'),
(2, 2, 70230, 'Doble Estándar: habitación para dos personas, con dos camas individuales o una cama doble.'),
(3, 2, 90560, 'Doble Superior: habitación para dos personas, camas King size, 50% más amplia que la Doble Estándar.'),
(4, 5, 110500, 'Superior Family Plan: capacidad para cinco personas, equipada con combinaciones de camas individuales y dobles.'),
(5, 2, 128600, 'Suite Doble: habitación asignada a una pareja.');

-- INDIVIDUAL ESTÁNDAR (10 habitaciones) tipo_habitacion_id = 1
INSERT INTO habitacion (id, tipo_habitacion_id, codigo) VALUES
(1, 1, 'IND-001'),
(2, 1, 'IND-002'),
(3, 1, 'IND-003'),
(4, 1, 'IND-004'),
(5, 1, 'IND-005'),
(6, 1, 'IND-006'),
(7, 1, 'IND-007'),
(8, 1, 'IND-008'),
(9, 1, 'IND-009'),
(10, 1, 'IND-010');

-- DOBLE ESTÁNDAR (18 habitaciones) tipo_habitacion_id = 2
INSERT INTO habitacion (id, tipo_habitacion_id, codigo) VALUES
(11, 2, 'DOB-001'),
(12, 2, 'DOB-002'),
(13, 2, 'DOB-003'),
(14, 2, 'DOB-004'),
(15, 2, 'DOB-005'),
(16, 2, 'DOB-006'),
(17, 2, 'DOB-007'),
(18, 2, 'DOB-008'),
(19, 2, 'DOB-009'),
(20, 2, 'DOB-010'),
(21, 2, 'DOB-011'),
(22, 2, 'DOB-012'),
(23, 2, 'DOB-013'),
(24, 2, 'DOB-014'),
(25, 2, 'DOB-015'),
(26, 2, 'DOB-016'),
(27, 2, 'DOB-017'),
(28, 2, 'DOB-018');

-- DOBLE SUPERIOR (8 habitaciones) tipo_habitacion_id = 3
INSERT INTO habitacion (id, tipo_habitacion_id, codigo) VALUES
(29, 3, 'SUP-001'),
(30, 3, 'SUP-002'),
(31, 3, 'SUP-003'),
(32, 3, 'SUP-004'),
(33, 3, 'SUP-005'),
(34, 3, 'SUP-006'),
(35, 3, 'SUP-007'),
(36, 3, 'SUP-008');

-- SUPERIOR FAMILY PLAN (10 habitaciones) tipo_habitacion_id = 4
INSERT INTO habitacion (id, tipo_habitacion_id, codigo) VALUES
(37, 4, 'FAM-001'),
(38, 4, 'FAM-002'),
(39, 4, 'FAM-003'),
(40, 4, 'FAM-004'),
(41, 4, 'FAM-005'),
(42, 4, 'FAM-006'),
(43, 4, 'FAM-007'),
(44, 4, 'FAM-008'),
(45, 4, 'FAM-009'),
(46, 4, 'FAM-010');

-- SUITE DOBLE (2 habitaciones) tipo_habitacion_id = 5
INSERT INTO habitacion (id, tipo_habitacion_id, codigo) VALUES
(47, 5, 'SUI-001'),
(48, 5, 'SUI-002');

INSERT INTO reserva (id, apellido, nombre, telefono, estado, fecha_inicio, fecha_fin, habitacion_id) VALUES
(1, 'Perez', 'Juan', '1111', 'NO_CONFIRMADA', '2025-12-29', '2026-01-02', 2),
(2, 'Lopez', 'Ana', '2222', 'NO_CONFIRMADA', '2025-12-30', '2026-01-05', 5),
(3, 'Suarez', 'Mario', '3333', 'NO_CONFIRMADA', '2025-12-31', '2026-01-04', 7),
(4, 'Diaz', 'Lucia', '4444', 'NO_CONFIRMADA', '2026-01-01', '2026-01-06', 9),
(5, 'Ramirez', 'Carla', '5555', 'NO_CONFIRMADA', '2026-01-02', '2026-01-07', 11),
(6, 'Martinez', 'Diego', '6666', 'NO_CONFIRMADA', '2026-01-03', '2026-01-08', 13),
(7, 'Perez', 'Juan', '1111', 'NO_CONFIRMADA', '2025-12-29', '2026-01-04', 16),
(8, 'Lopez', 'Ana', '2222', 'NO_CONFIRMADA', '2025-12-30', '2026-01-03', 18),
(9, 'Suarez', 'Mario', '3333', 'NO_CONFIRMADA', '2025-12-31', '2026-01-06', 20),
(10, 'Diaz', 'Lucia', '4444', 'NO_CONFIRMADA', '2026-01-01', '2026-01-05', 22),
(11, 'Ramirez', 'Carla', '5555', 'NO_CONFIRMADA', '2026-01-02', '2026-01-09', 27),
(12, 'Martinez', 'Diego', '6666', 'NO_CONFIRMADA', '2026-01-03', '2026-01-10', 31),
(13, 'Perez', 'Juan', '1111', 'NO_CONFIRMADA', '2025-12-29', '2026-01-03', 34),
(14, 'Lopez', 'Ana', '2222', 'NO_CONFIRMADA', '2025-12-30', '2026-01-06', 39),
(15, 'Suarez', 'Mario', '3333', 'NO_CONFIRMADA', '2025-12-31', '2026-01-08', 43),
(16, 'Diaz', 'Lucia', '4444', 'NO_CONFIRMADA', '2026-01-01', '2026-01-11', 47);

INSERT INTO estadia (id, fecha_inicio, fecha_fin, habitacion_id, reserva_id) VALUES
(1, '2025-12-29', '2026-01-03', 1, NULL),
(2, '2025-12-31', '2026-01-05', 4, NULL),
(3, '2025-12-30', '2026-01-06', 6, NULL),
(4, '2026-01-01', '2026-01-08', 8, NULL),
(5, '2026-01-02', '2026-01-09', 12, NULL),
(6, '2026-01-03', '2026-01-07', 14, NULL),
(7, '2025-12-29', '2026-01-04', 17, NULL),
(8, '2025-12-30', '2026-01-05', 19, NULL),
(9, '2025-12-31', '2026-01-08', 23, NULL),
(10, '2026-01-01', '2026-01-06', 25, NULL),
(11, '2026-01-02', '2026-01-10', 28, NULL),
(12, '2026-01-03', '2026-01-11', 30, NULL),
(13, '2025-12-29', '2026-01-02', 33, NULL),
(14, '2025-12-31', '2026-01-09', 38, NULL),
(15, '2026-01-01', '2026-01-07', 41, NULL),
(16, '2026-01-02', '2026-01-12', 46, NULL);

INSERT INTO no_disponibilidad_habitacion (id, fecha_inicio, fecha_fin, motivo, habitacion_id) VALUES
(1, '2025-12-29', '2026-01-03', 'MANTENIMIENTO', 3),
(2, '2026-01-01', '2026-01-05', 'MANTENIMIENTO', 10),
(3, '2025-12-31', '2026-01-04', 'MANTENIMIENTO', 15),
(4, '2026-01-02', '2026-01-08', 'MANTENIMIENTO', 21),
(5, '2026-01-03', '2026-01-09', 'MANTENIMIENTO', 24),
(6, '2025-12-29', '2026-01-02', 'MANTENIMIENTO', 26),
(7, '2025-12-30', '2026-01-05', 'MANTENIMIENTO', 29),
(8, '2026-01-01', '2026-01-07', 'MANTENIMIENTO', 32),
(9, '2025-12-31', '2026-01-06', 'MANTENIMIENTO', 35),
(10, '2026-01-02', '2026-01-10', 'MANTENIMIENTO', 36),
(11, '2026-01-03', '2026-01-08', 'MANTENIMIENTO', 37),
(12, '2025-12-29', '2026-01-04', 'MANTENIMIENTO', 40),
(13, '2025-12-30', '2026-01-03', 'MANTENIMIENTO', 42),
(14, '2025-12-31', '2026-01-06', 'MANTENIMIENTO', 44),
(15, '2026-01-01', '2026-01-05', 'MANTENIMIENTO', 45),
(16, '2026-01-02', '2026-01-09', 'MANTENIMIENTO', 48);
