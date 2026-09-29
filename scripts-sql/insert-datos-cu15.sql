-- Scripts de inserción de datos para probar CU15 "Ocupar Habitación"

-- 1. TIPOS DE HABITACIÓN
INSERT INTO tipo_habitacion (id, nombre, descripcion, precio_base) VALUES
(1, 'Simple', 'Habitación individual con cama simple', 5000.00),
(2, 'Doble', 'Habitación con cama matrimonial', 7500.00),
(3, 'Triple', 'Habitación con tres camas individuales', 9500.00),
(4, 'Suite', 'Suite con jacuzzi y vista panorámica', 15000.00);

-- 2. HABITACIONES
INSERT INTO habitacion (id, codigo, tipo_habitacion_id) VALUES
(1, 'H101', 1),
(2, 'H102', 1),
(3, 'H201', 2),
(4, 'H202', 2),
(5, 'H301', 3),
(6, 'H401', 4);

-- 3. TIPOS DE CAMA PARA HABITACIONES (ElementCollection)
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(1, 'INDIVIDUAL_ESTANDAR'),
(2, 'INDIVIDUAL_ESTANDAR'),
(3, 'DOBLE_ESTANDAR'),
(4, 'DOBLE_ESTANDAR'),
(5, 'INDIVIDUAL_ESTANDAR'),
(5, 'INDIVIDUAL_ESTANDAR'),
(5, 'INDIVIDUAL_ESTANDAR'),
(6, 'KING_SIZE'),
(6, 'INDIVIDUAL_ESTANDAR');

-- 4. RESERVAS DE PRUEBA (para conflictos)
INSERT INTO reserva (id, nombre, apellido, telefono, fecha_inicio, fecha_fin, estado, habitacion_id) VALUES
(1, 'Juan', 'González', '1122334455', '2024-01-16', '2024-01-18', 'CONFIRMADA', 3),
(2, 'María', 'Pérez', '1122334466', '2024-01-20', '2024-01-22', 'NO_CONFIRMADA', 4),
(3, 'Carlos', 'López', '1133445566', '2024-01-25', '2024-01-27', 'CONFIRMADA', 1);

-- 5. ESTADÍAS EXISTENTES (habitaciones ocupadas)
INSERT INTO estadia (id, fecha_inicio, fecha_fin, habitacion_id, reserva_id) VALUES
(1, '2024-01-12', '2024-01-15', 2, NULL),
(2, '2024-01-18', '2024-01-21', 5, NULL);

-- 6. RELACIÓN ESTADIA-HUESPED (ManyToMany)
-- Nota: Los IDs de huéspedes deben corresponder con los del insert-huespedes.sql
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(1, 1), -- González Juan en habitación 2
(1, 2), -- Pérez María acompañando
(2, 3), -- López Carlos en habitación 5
(2, 4), -- Fernández Sofía acompañando
(2, 5); -- García Lucía acompañando

-- 7. NO DISPONIBILIDADES (mantenimiento/bloqueos)
INSERT INTO no_disponibilidad_habitacion (id, motivo, fecha_inicio, fecha_fin, habitacion_id) VALUES
(1, 'Mantenimiento aire acondicionado', '2024-01-14', '2024-01-16', 6),
(2, 'Reparación baño', '2024-01-23', '2024-01-24', 1),
(3, 'Fumigación', '2024-01-30', '2024-02-01', 3);