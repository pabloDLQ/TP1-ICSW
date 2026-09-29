-- ================================================================================
-- SCRIPT DE POBLACIÓN COMPLETA - HOTEL PREMIER
-- ================================================================================
-- Este script pobla toda la base de datos con datos consistentes para probar
-- todos los endpoints del sistema.
--
-- Configurar charset UTF-8 para manejar correctamente caracteres especiales
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ORDEN DE EJECUCIÓN:
-- 1. Direcciones
-- 2. Tipos de Habitación y Habitaciones
-- 3. Huéspedes
-- 4. Reservas
-- 5. Estadías
-- 6. Relación Estadía-Huésped
-- 7. Gastos Adicionales
-- 8. Responsables de Pago (Personas Físicas y Jurídicas)
-- 9. Facturas
-- 10. No Disponibilidades
-- ================================================================================

-- ================================================================================
-- 1. DIRECCIONES
-- ================================================================================
INSERT INTO direccion (id, calle, numero, piso, nro_departamento, codigo_postal, localidad, provincia, pais) VALUES
(1, 'Av. Corrientes', '1234', '5', 'A', '1043', 'Buenos Aires', 'CABA', 'Argentina'),
(2, 'Calle Falsa', '742', NULL, NULL, '5000', 'Córdoba', 'Córdoba', 'Argentina'),
(3, 'San Martín', '456', '2', 'B', '2000', 'Rosario', 'Santa Fe', 'Argentina'),
(4, 'Mitre', '890', NULL, NULL, '3100', 'Paraná', 'Entre Ríos', 'Argentina'),
(5, 'Belgrano', '321', '10', 'C', '4000', 'San Miguel de Tucumán', 'Tucumán', 'Argentina'),
(6, 'Rivadavia', '567', NULL, NULL, '5500', 'Mendoza', 'Mendoza', 'Argentina'),
(7, 'Av. Libertador', '2100', '3', '5', '1425', 'Buenos Aires', 'CABA', 'Argentina'),
(8, '9 de Julio', '777', NULL, NULL, '7600', 'Mar del Plata', 'Buenos Aires', 'Argentina'),
(9, 'Sarmiento', '1550', '7', 'D', '1041', 'Buenos Aires', 'CABA', 'Argentina'),
(10, 'Pellegrini', '333', NULL, NULL, '5000', 'Córdoba', 'Córdoba', 'Argentina'),
-- Direcciones para empresas
(11, 'Av. Del Libertador', '1000', '15', NULL, '1112', 'Buenos Aires', 'CABA', 'Argentina'),
(12, 'Maipú', '234', '8', 'B', '5000', 'Córdoba', 'Córdoba', 'Argentina'),
(13, 'Av. 9 de Julio', '500', NULL, NULL, '2000', 'Rosario', 'Santa Fe', 'Argentina');

-- ================================================================================
-- 2. TIPOS DE HABITACIÓN Y HABITACIONES
-- ================================================================================

-- Tipos de Habitación
INSERT INTO tipo_habitacion (id, capacidad, costo_por_noche, descripcion) VALUES
(1, 1, 50800, 'Individual Estándar: habitación para una persona con cama individual.'),
(2, 2, 70230, 'Doble Estándar: habitación para dos personas, con dos camas individuales o una cama doble.'),
(3, 2, 90560, 'Doble Superior: habitación para dos personas, camas King size, 50% más amplia que la Doble Estándar.'),
(4, 5, 110500, 'Superior Family Plan: capacidad para cinco personas, equipada con combinaciones de camas individuales y dobles.'),
(5, 2, 128600, 'Suite Doble: habitación premium asignada a una pareja con servicios VIP.');

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

-- ================================================================================
-- 2.1 CAMAS DE LAS HABITACIONES (ElementCollection)
-- ================================================================================

-- INDIVIDUAL ESTÁNDAR (habitaciones 1-10): 1 cama individual cada una
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(1, 'INDIVIDUAL_ESTANDAR'),
(2, 'INDIVIDUAL_ESTANDAR'),
(3, 'INDIVIDUAL_ESTANDAR'),
(4, 'INDIVIDUAL_ESTANDAR'),
(5, 'INDIVIDUAL_ESTANDAR'),
(6, 'INDIVIDUAL_ESTANDAR'),
(7, 'INDIVIDUAL_ESTANDAR'),
(8, 'INDIVIDUAL_ESTANDAR'),
(9, 'INDIVIDUAL_ESTANDAR'),
(10, 'INDIVIDUAL_ESTANDAR');

-- DOBLE ESTÁNDAR (habitaciones 11-28): 1 cama doble cada una
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(11, 'DOBLE_ESTANDAR'),
(12, 'DOBLE_ESTANDAR'),
(13, 'DOBLE_ESTANDAR'),
(14, 'DOBLE_ESTANDAR'),
(15, 'DOBLE_ESTANDAR'),
(16, 'DOBLE_ESTANDAR'),
(17, 'DOBLE_ESTANDAR'),
(18, 'DOBLE_ESTANDAR'),
(19, 'DOBLE_ESTANDAR'),
(20, 'DOBLE_ESTANDAR'),
(21, 'DOBLE_ESTANDAR'),
(22, 'DOBLE_ESTANDAR'),
(23, 'DOBLE_ESTANDAR'),
(24, 'DOBLE_ESTANDAR'),
(25, 'DOBLE_ESTANDAR'),
(26, 'DOBLE_ESTANDAR'),
(27, 'DOBLE_ESTANDAR'),
(28, 'DOBLE_ESTANDAR');

-- DOBLE SUPERIOR (habitaciones 29-36): 1 cama King Size cada una
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(29, 'KING_SIZE'),
(30, 'KING_SIZE'),
(31, 'KING_SIZE'),
(32, 'KING_SIZE'),
(33, 'KING_SIZE'),
(34, 'KING_SIZE'),
(35, 'KING_SIZE'),
(36, 'KING_SIZE');

-- SUPERIOR FAMILY PLAN (habitaciones 37-46): 2 camas individuales + 1 cama doble
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(37, 'INDIVIDUAL_ESTANDAR'),
(37, 'INDIVIDUAL_ESTANDAR'),
(37, 'DOBLE_ESTANDAR'),
(38, 'INDIVIDUAL_ESTANDAR'),
(38, 'INDIVIDUAL_ESTANDAR'),
(38, 'DOBLE_ESTANDAR'),
(39, 'INDIVIDUAL_ESTANDAR'),
(39, 'INDIVIDUAL_ESTANDAR'),
(39, 'DOBLE_ESTANDAR'),
(40, 'INDIVIDUAL_ESTANDAR'),
(40, 'INDIVIDUAL_ESTANDAR'),
(40, 'DOBLE_ESTANDAR'),
(41, 'INDIVIDUAL_ESTANDAR'),
(41, 'INDIVIDUAL_ESTANDAR'),
(41, 'DOBLE_ESTANDAR'),
(42, 'INDIVIDUAL_ESTANDAR'),
(42, 'INDIVIDUAL_ESTANDAR'),
(42, 'DOBLE_ESTANDAR'),
(43, 'INDIVIDUAL_ESTANDAR'),
(43, 'INDIVIDUAL_ESTANDAR'),
(43, 'DOBLE_ESTANDAR'),
(44, 'INDIVIDUAL_ESTANDAR'),
(44, 'INDIVIDUAL_ESTANDAR'),
(44, 'DOBLE_ESTANDAR'),
(45, 'INDIVIDUAL_ESTANDAR'),
(45, 'INDIVIDUAL_ESTANDAR'),
(45, 'DOBLE_ESTANDAR'),
(46, 'INDIVIDUAL_ESTANDAR'),
(46, 'INDIVIDUAL_ESTANDAR'),
(46, 'DOBLE_ESTANDAR');

-- SUITE DOBLE (habitaciones 47-48): 1 cama King Size + 1 cama individual
INSERT INTO habitacion_camas (habitacion_id, tipo_cama) VALUES
(47, 'KING_SIZE'),
(47, 'INDIVIDUAL_ESTANDAR'),
(48, 'KING_SIZE'),
(48, 'INDIVIDUAL_ESTANDAR');

-- ================================================================================
-- 3. HUÉSPEDES
-- ================================================================================
INSERT INTO huesped 
(id, apellido, nombre, tipo_documento, numero_documento, cuit, posicion_iva, fecha_nacimiento, direccion_id, telefono, email, ocupacion, nacionalidad)
VALUES
(1, 'González', 'Juan', 'DNI', '30111222', '20-30111222-5', 'CONSUMIDOR_FINAL', '1990-04-12', 1, '1122334455', 'juan.gonzalez@mail.com', 'Ingeniero de Sistemas', 'Argentina'),
(2, 'Pérez', 'María', 'DNI', '28999111', '27-28999111-2', 'EXENTO', '1988-07-20', 2, '1122334466', 'maria.perez@mail.com', 'Contadora Pública', 'Argentina'),
(3, 'López', 'Carlos', 'PASAPORTE', 'AB123456', '20-20123456-9', 'RESPONSABLE_INSCRIPTO', '1985-02-15', 3, '1133445566', 'carlos.lopez@mail.com', 'Ingeniero Civil', 'Argentina'),
(4, 'Fernández', 'Sofía', 'DNI', '31222333', '27-31222333-4', 'EXENTO', '1993-10-05', 4, '1144556677', 'sofia.fernandez@mail.com', 'Diseñadora Gráfica', 'Argentina'),
(5, 'García', 'Lucía', 'DNI', '29888777', '27-29888777-1', 'CONSUMIDOR_FINAL', '1998-01-19', 5, '1166778899', 'lucia.garcia@mail.com', 'Estudiante', 'Argentina'),
(6, 'Martínez', 'Diego', 'DNI', '30000111', '27-30000111-3', 'RESPONSABLE_INSCRIPTO', '1991-12-22', 6, '1155998844', 'diego.martinez@mail.com', 'Arquitecto', 'Argentina'),
(7, 'Rodríguez', 'Ana', 'DNI', '27777123', '20-27777123-0', 'CONSUMIDOR_FINAL', '1982-09-17', 7, '1177889900', 'ana.rodriguez@mail.com', 'Programadora', 'Argentina'),
(8, 'Sánchez', 'Lucía', 'PASAPORTE', 'CD987654', '27-20987654-7', 'EXENTO', '1997-03-28', 8, '1188990077', 'lucia.sanchez@mail.com', 'Diseñadora de Interiores', 'Argentina'),
(9, 'Rojas', 'Federico', 'DNI', '33123456', '20-33123456-1', 'RESPONSABLE_INSCRIPTO', '1989-11-11', 9, '1199001122', 'federico.rojas@mail.com', 'Abogado', 'Argentina'),
(10, 'Torres', 'Ana', 'DNI', '32222111', '27-32222111-5', 'CONSUMIDOR_FINAL', '1994-08-09', 10, '1100998877', 'ana.torres@mail.com', 'Chef', 'Argentina'),
(11, 'Morales', 'Juan', 'DNI', '29999123', '20-29999123-3', 'EXENTO', '1991-05-14', 1, '1199554433', 'juan.morales@mail.com', 'Docente', 'Argentina'),
(12, 'Gómez', 'Pedro', 'LE', '14567890', '20-14567890-2', 'RESPONSABLE_INSCRIPTO', '1979-12-01', 2, '1144112255', 'pedro.gomez@mail.com', 'Comerciante', 'Argentina'),
(13, 'Díaz', 'Mariana', 'DNI', '30123456', '27-30123456-8', 'CONSUMIDOR_FINAL', '1996-06-30', 3, '1188223344', 'mariana.diaz@mail.com', 'Estudiante de Medicina', 'Argentina'),
(14, 'Romero', 'Martín', 'PASAPORTE', 'EF456789', '20-22334455-6', 'EXENTO', '1984-03-03', 4, '1177554411', 'martin.romero@mail.com', 'Empleado Bancario', 'Argentina'),
(15, 'Vargas', 'Carla', 'DNI', '28889911', '27-28889911-4', 'RESPONSABLE_INSCRIPTO', '1992-02-07', 5, '1166335522', 'carla.vargas@mail.com', 'Arquitecta', 'Argentina'),
(16, 'Silva', 'Carlos', 'DNI', '27777001', '20-27777001-9', 'CONSUMIDOR_FINAL', '1987-07-21', 6, '1155778899', 'carlos.silva@mail.com', 'Analista de Datos', 'Argentina'),
(17, 'Ramos', 'Lucía', 'LC', '9876543', '27-19876543-4', 'EXENTO', '1999-04-02', 7, '1144889966', 'lucia.ramos@mail.com', 'Diseñadora UX', 'Argentina'),
(18, 'Castro', 'Ana', 'DNI', '32123456', '27-32123456-7', 'RESPONSABLE_INSCRIPTO', '1986-05-23', 8, '1122113344', 'ana.castro@mail.com', 'Enfermera', 'Argentina'),
(19, 'Molina', 'Juan', 'DNI', '30987654', '20-30987654-6', 'CONSUMIDOR_FINAL', '1990-09-12', 9, '1100112233', 'juan.molina@mail.com', 'Mozo', 'Argentina'),
(20, 'Herrera', 'Sofía', 'PASAPORTE', 'GH112233', '27-26543210-9', 'EXENTO', '1995-11-18', 10, '1133557799', 'sofia.herrera@mail.com', 'Estudiante de Derecho', 'Argentina'),
(21, 'Alvarez', 'Roberto', 'DNI', '31555666', '20-31555666-3', 'CONSUMIDOR_FINAL', '1992-08-25', 1, '1155443322', 'roberto.alvarez@mail.com', 'Vendedor', 'Argentina'),
(22, 'Benítez', 'Laura', 'DNI', '29333444', '27-29333444-1', 'RESPONSABLE_INSCRIPTO', '1989-11-30', 2, '1166554433', 'laura.benitez@mail.com', 'Psicóloga', 'Argentina'),
(23, 'Campos', 'Miguel', 'DNI', '32666777', '20-32666777-8', 'EXENTO', '1994-04-18', 3, '1177665544', 'miguel.campos@mail.com', 'Fotógrafo', 'Argentina'),
(24, 'Domínguez', 'Paula', 'DNI', '28111222', '27-28111222-2', 'CONSUMIDOR_FINAL', '1987-01-09', 4, '1188776655', 'paula.dominguez@mail.com', 'Periodista', 'Argentina'),
(25, 'Espinoza', 'Alejandro', 'PASAPORTE', 'IJ789012', '20-25789012-4', 'RESPONSABLE_INSCRIPTO', '1986-05-12', 5, '1199887766', 'alejandro.espinoza@mail.com', 'Gerente', 'Argentina');

-- ================================================================================
-- 4. RESERVAS
-- ================================================================================
-- Reservas CONFIRMADAS (algunas ya pasadas, otras futuras)
INSERT INTO reserva (id, apellido, nombre, telefono, estado, fecha_inicio, fecha_fin, habitacion_id) VALUES
(1, 'González', 'Juan', '1122334455', 'CONFIRMADA', '2025-12-10', '2025-12-15', 1),
(2, 'Pérez', 'María', '1122334466', 'CONFIRMADA', '2025-12-12', '2025-12-16', 11),
(3, 'López', 'Carlos', '1133445566', 'CONFIRMADA', '2025-12-18', '2025-12-22', 29),
(4, 'Fernández', 'Sofía', '1144556677', 'CONFIRMADA', '2025-12-20', '2025-12-25', 37),
(5, 'García', 'Lucía', '1166778899', 'CONFIRMADA', '2025-12-22', '2025-12-28', 47);

-- Reservas NO_CONFIRMADAS (futuras)
INSERT INTO reserva (id, apellido, nombre, telefono, estado, fecha_inicio, fecha_fin, habitacion_id) VALUES
(6, 'Martínez', 'Diego', '1155998844', 'NO_CONFIRMADA', '2025-12-28', '2026-01-02', 2),
(7, 'Rodríguez', 'Ana', '1177889900', 'NO_CONFIRMADA', '2025-12-30', '2026-01-05', 5),
(8, 'Sánchez', 'Lucía', '1188990077', 'NO_CONFIRMADA', '2026-01-05', '2026-01-10', 12),
(9, 'Rojas', 'Federico', '1199001122', 'NO_CONFIRMADA', '2026-01-08', '2026-01-12', 20),
(10, 'Torres', 'Ana', '1100998877', 'NO_CONFIRMADA', '2026-01-10', '2026-01-15', 30);

-- Reservas para probar conflictos
INSERT INTO reserva (id, apellido, nombre, telefono, estado, fecha_inicio, fecha_fin, habitacion_id) VALUES
(11, 'Morales', 'Juan', '1199554433', 'CONFIRMADA', '2026-01-20', '2026-01-25', 3),
(12, 'Gómez', 'Pedro', '1144112255', 'NO_CONFIRMADA', '2026-01-22', '2026-01-27', 3),
(13, 'Díaz', 'Mariana', '1188223344', 'CONFIRMADA', '2026-02-01', '2026-02-05', 15),
(14, 'Romero', 'Martín', '1177554411', 'NO_CONFIRMADA', '2026-02-10', '2026-02-15', 35),
(15, 'Vargas', 'Carla', '1166335522', 'CONFIRMADA', '2026-02-12', '2026-02-18', 45);

-- ================================================================================
-- 5. ESTADÍAS (Check-ins realizados)
-- ================================================================================
-- Estadías FINALIZADAS (ya terminadas, listas para facturar)
INSERT INTO estadia (id, fecha_inicio, fecha_fin, habitacion_id, reserva_id) VALUES
(1, '2025-11-01', '2025-11-05', 1, NULL),
(2, '2025-11-10', '2025-11-15', 11, NULL),
(3, '2025-11-20', '2025-11-25', 29, NULL),
(4, '2025-12-01', '2025-12-05', 37, NULL),
(5, '2025-12-05', '2025-12-10', 47, NULL);

-- Estadías ACTIVAS (en curso)
INSERT INTO estadia (id, fecha_inicio, fecha_fin, habitacion_id, reserva_id) VALUES
(6, '2025-12-10', '2025-12-20', 2, 1),
(7, '2025-12-12', '2025-12-18', 12, 2),
(8, '2025-12-14', '2025-12-22', 30, NULL),
(9, '2025-12-15', '2025-12-25', 38, NULL),
(10, '2025-12-16', '2025-12-24', 48, NULL);

-- Estadías FUTURAS (check-in programado)
INSERT INTO estadia (id, fecha_inicio, fecha_fin, habitacion_id, reserva_id) VALUES
(11, '2026-01-05', '2026-01-10', 4, NULL),
(12, '2026-01-12', '2026-01-17', 14, NULL),
(13, '2026-01-20', '2026-01-25', 24, NULL);

-- ================================================================================
-- 6. RELACIÓN ESTADÍA-HUÉSPED (ManyToMany)
-- ================================================================================
-- Estadía 1: huésped principal + 1 acompañante
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(1, 1),  -- González Juan (principal)
(1, 2);  -- Pérez María (acompañante)

-- Estadía 2: huésped principal solo
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(2, 3);  -- López Carlos (solo)

-- Estadía 3: huésped principal + 1 acompañante
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(3, 4),  -- Fernández Sofía (principal)
(3, 5);  -- García Lucía (acompañante)

-- Estadía 4: familia de 4 personas
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(4, 6),   -- Martínez Diego (principal)
(4, 7),   -- Rodríguez Ana (cónyuge)
(4, 21),  -- Alvarez Roberto (hijo)
(4, 22);  -- Benítez Laura (hija)

-- Estadía 5: pareja en suite
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(5, 8),  -- Sánchez Lucía (principal)
(5, 9);  -- Rojas Federico (pareja)

-- Estadía 6: huésped solo
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(6, 10); -- Torres Ana (solo)

-- Estadía 7: pareja
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(7, 11), -- Morales Juan
(7, 12); -- Gómez Pedro

-- Estadía 8: huésped + 1 acompañante
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(8, 13), -- Díaz Mariana
(8, 14); -- Romero Martín

-- Estadía 9: familia de 5
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(9, 15), -- Vargas Carla
(9, 16), -- Silva Carlos
(9, 23), -- Campos Miguel
(9, 24), -- Domínguez Paula
(9, 25); -- Espinoza Alejandro

-- Estadía 10: pareja en suite
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(10, 17), -- Ramos Lucía
(10, 18); -- Castro Ana

-- Estadía 11: huésped solo
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(11, 19); -- Molina Juan

-- Estadía 12: pareja
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(12, 20), -- Herrera Sofía
(12, 1);  -- González Juan

-- Estadía 13: huésped + 2 acompañantes
INSERT INTO estadia_huesped (estadia_id, huesped_id) VALUES
(13, 2),  -- Pérez María
(13, 3),  -- López Carlos
(13, 4);  -- Fernández Sofía

-- ================================================================================
-- 7. GASTOS ADICIONALES
-- ================================================================================
-- Gastos para estadía 1 (finalizada)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(1, 2500.00, 'Room Service - Desayuno continental', 'SERVICIO', 1),
(2, 1800.00, 'Lavandería - Servicio express', 'SERVICIO', 1),
(3, 3200.00, 'Minibar - Bebidas y snacks', 'CONSUMO', 1);

-- Gastos para estadía 2 (finalizada)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(4, 5000.00, 'Room Service - Cena para dos', 'SERVICIO', 2),
(5, 2200.00, 'Lavandería - 5 prendas', 'SERVICIO', 2),
(6, 1500.00, 'Minibar - Champagne', 'CONSUMO', 2),
(7, 8000.00, 'Spa - Masajes relajantes', 'SERVICIO', 2);

-- Gastos para estadía 3 (finalizada)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(8, 3500.00, 'Room Service - Almuerzo', 'SERVICIO', 3),
(9, 2800.00, 'Minibar - Vino y aperitivos', 'CONSUMO', 3);

-- Gastos para estadía 4 (finalizada)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(10, 12000.00, 'Room Service - Cena familiar', 'SERVICIO', 4),
(11, 4500.00, 'Lavandería - Servicio completo familia', 'SERVICIO', 4),
(12, 6000.00, 'Minibar - Consumos varios', 'CONSUMO', 4),
(13, 15000.00, 'Spa - Tratamiento día completo (4 personas)', 'SERVICIO', 4);

-- Gastos para estadía 5 (finalizada)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(14, 8500.00, 'Room Service - Cena romántica', 'SERVICIO', 5),
(15, 5500.00, 'Minibar - Champagne premium', 'CONSUMO', 5),
(16, 18000.00, 'Spa - Masajes de pareja', 'SERVICIO', 5),
(17, 3000.00, 'Lavandería - Servicio premium', 'SERVICIO', 5);

-- Gastos para estadías activas (6-10)
INSERT INTO gasto_adicional (id, precio, descripcion, tipo_gasto_adicional, estadia_id) VALUES
(18, 2200.00, 'Room Service - Desayuno', 'SERVICIO', 6),
(19, 1800.00, 'Minibar - Bebidas', 'CONSUMO', 6),
(20, 3500.00, 'Room Service - Almuerzo ejecutivo', 'SERVICIO', 7),
(21, 2500.00, 'Lavandería - 3 prendas', 'SERVICIO', 7),
(22, 4200.00, 'Room Service - Cena', 'SERVICIO', 8),
(23, 3800.00, 'Minibar - Vinos premium', 'CONSUMO', 8),
(24, 9000.00, 'Room Service - Catering grupo', 'SERVICIO', 9),
(25, 5500.00, 'Minibar - Consumos familia', 'CONSUMO', 9),
(26, 6800.00, 'Room Service - Cena premium', 'SERVICIO', 10),
(27, 4200.00, 'Minibar - Champagne y canapés', 'CONSUMO', 10),
(28, 12000.00, 'Spa - Tratamiento completo pareja', 'SERVICIO', 10);

-- ================================================================================
-- 8. RESPONSABLES DE PAGO
-- ================================================================================

-- 8.1 PERSONAS FÍSICAS (huéspedes que pagan sus facturas)
-- Se crean en la tabla responsable_pago y luego se referencian en persona_fisica
INSERT INTO responsable_pago (id) VALUES
(1), (2), (3), (4), (5), (6), (7), (8), (9), (10);

INSERT INTO persona_fisica (id, huesped_id) VALUES
(1, 1),   -- González Juan
(2, 3),   -- López Carlos
(3, 4),   -- Fernández Sofía
(4, 6),   -- Martínez Diego
(5, 8),   -- Sánchez Lucía
(6, 10),  -- Torres Ana
(7, 11),  -- Morales Juan
(8, 13),  -- Díaz Mariana
(9, 15),  -- Vargas Carla
(10, 17); -- Ramos Lucía

-- 8.2 PERSONAS JURÍDICAS (empresas que pagan facturas)
INSERT INTO responsable_pago (id) VALUES
(11), (12), (13), (14), (15);

INSERT INTO persona_juridica (id, razon_social, cuit, posicion_iva, direccion_id) VALUES
(11, 'Tech Solutions S.A.', '30-71234567-8', 'RESPONSABLE_INSCRIPTO', 11),
(12, 'Consultora Empresarial SRL', '30-78901234-5', 'RESPONSABLE_INSCRIPTO', 12),
(13, 'Constructora del Sur S.A.', '30-76543210-2', 'RESPONSABLE_INSCRIPTO', 13),
(14, 'Servicios Integrales SA', '30-72468135-9', 'RESPONSABLE_INSCRIPTO', 11),
(15, 'Logística Argentina SRL', '30-79876543-3', 'RESPONSABLE_INSCRIPTO', 12);

-- ================================================================================
-- 9. FACTURAS
-- ================================================================================
-- Facturas PAGADAS para estadías finalizadas
INSERT INTO factura (id, monto_factura, tipo, estado, fecha_emision, responsable_pago_id, estadia_id) VALUES
(1, 261800.00, 'B', 'PAGADA', '2025-11-05', 1, 1),     -- Estadía 1: 5 noches * 50800 + gastos (7500)
(2, 368050.00, 'A', 'PAGADA', '2025-11-15', 2, 2),     -- Estadía 2: 5 noches * 70230 + gastos (16700)
(3, 459100.00, 'B', 'PAGADA', '2025-11-25', 3, 3);     -- Estadía 3: 5 noches * 90560 + gastos (6300)

-- Facturas NO_PAGADAS para estadías finalizadas (pendientes de cobro)
INSERT INTO factura (id, monto_factura, tipo, estado, fecha_emision, responsable_pago_id, estadia_id) VALUES
(4, 590000.00, 'A', 'NO_PAGADA', '2025-12-05', 4, 4),   -- Estadía 4: 4 noches * 110500 + gastos (37500)
(5, 678200.00, 'B', 'NO_PAGADA', '2025-12-10', 5, 5);   -- Estadía 5: 5 noches * 128600 + gastos (35000)

-- Facturas para empresas (TIPO A siempre)
INSERT INTO factura (id, monto_factura, tipo, estado, fecha_emision, responsable_pago_id, estadia_id) VALUES
(6, 512000.00, 'A', 'PAGADA', '2025-11-20', 11, 1),     -- Empresa paga estadía 1
(7, 351150.00, 'A', 'NO_PAGADA', '2025-12-08', 12, 2),  -- Empresa paga estadía 2
(8, 459100.00, 'A', 'PAGADA', '2025-12-01', 13, 3);     -- Empresa paga estadía 3

-- ================================================================================
-- 10. NO DISPONIBILIDADES (Mantenimiento y bloqueos)
-- ================================================================================
INSERT INTO no_disponibilidad_habitacion (id, motivo, fecha_inicio, fecha_fin, habitacion_id) VALUES
(1, 'Mantenimiento preventivo aire acondicionado', '2025-12-26', '2025-12-28', 6),
(2, 'Reparación sistema eléctrico', '2025-12-27', '2025-12-29', 13),
(3, 'Fumigación programada', '2026-01-02', '2026-01-03', 25),
(4, 'Renovación de mobiliario', '2026-01-05', '2026-01-08', 32),
(5, 'Mantenimiento baño y plomería', '2026-01-10', '2026-01-12', 40),
(6, 'Pintura y refacción', '2026-01-15', '2026-01-20', 7),
(7, 'Cambio de alfombras', '2026-01-18', '2026-01-20', 16),
(8, 'Reparación ventanas', '2026-01-22', '2026-01-24', 28),
(9, 'Mantenimiento suite completo', '2026-02-01', '2026-02-05', 47),
(10, 'Inspección de seguridad', '2026-02-10', '2026-02-11', 35);

-- ================================================================================
-- FIN DEL SCRIPT
-- ================================================================================
-- RESUMEN DE DATOS INSERTADOS:
-- - 13 Direcciones
-- - 5 Tipos de Habitación
-- - 48 Habitaciones
-- - 25 Huéspedes
-- - 15 Reservas (CONFIRMADAS y NO_CONFIRMADAS)
-- - 13 Estadías (finalizadas, activas y futuras)
-- - Relaciones Estadía-Huésped para todas las estadías
-- - 28 Gastos Adicionales
-- - 10 Personas Físicas como responsables
-- - 5 Personas Jurídicas como responsables
-- - 8 Facturas (PAGADAS y NO_PAGADAS, tipos A y B)
-- - 10 No Disponibilidades
-- ================================================================================

-- CASOS DE USO CUBIERTOS PARA TESTING:

-- 1. GESTIÓN DE HUÉSPEDES:
--    - GET /huespedes (25 huéspedes disponibles)
--    - POST /huespedes (crear nuevos)
--    - PUT /huespedes (modificar existentes)
--    - DELETE /huespedes (eliminar no asociados a estadías)

-- 2. GESTIÓN DE RESERVAS:
--    - POST /reservas (crear nueva)
--    - GET /reservas (listar todas - 15 disponibles)
--    - Reservas confirmadas y no confirmadas
--    - Reservas con conflictos de fechas (IDs 11 y 12 en misma habitación)

-- 3. DISPONIBILIDAD DE HABITACIONES:
--    - GET /habitaciones/disponibilidad?inicio=YYYY-MM-DD&fin=YYYY-MM-DD
--    - Habitaciones ocupadas por estadías activas
--    - Habitaciones bloqueadas por mantenimiento
--    - Habitaciones con reservas futuras

-- 4. CONFLICTOS CON RESERVAS:
--    - GET /habitaciones/conflictos-reservas?habitacionId=3&inicio=2026-01-21&fin=2026-01-26
--    - Probar con habitación 3 que tiene reservas solapadas

-- 5. CHECK-IN (OCUPAR HABITACIÓN):
--    - POST /estadias
--    - Múltiples huéspedes en una estadía
--    - Check-in con/sin reserva asociada
--    - Validar capacidad de habitación

-- 6. FACTURACIÓN:
--    - GET /facturacion/estadia/huespedes?idHabitacion=1&fechaFin=2025-11-05
--    - POST /facturacion/estadia/{estadiaId}/preview (previsualizar monto)
--    - POST /facturacion (generar factura)
--    - Facturas tipo A y B según posición IVA
--    - Responsables persona física y jurídica

-- 7. GASTOS ADICIONALES:
--    - Estadías con múltiples gastos (SERVICIO: Room Service, Lavandería, SPA | CONSUMO: Minibar)
--    - Cálculo de monto total con gastos incluidos

-- ESCENARIOS DE PRUEBA ESPECÍFICOS:

-- A. Disponibilidad con múltiples restricciones:
--    Fecha: 2025-12-27 a 2025-12-29
--    - Habitaciones 6 y 13 en mantenimiento
--    - Habitaciones 2, 12, 30, 38, 48 ocupadas

-- B. Facturación estadía con múltiples huéspedes:
--    - Estadía 4: familia de 4 personas, 4 noches, múltiples gastos
--    - Seleccionar responsable de pago entre los 4 huéspedes
--    - O asignar a empresa

-- C. Check-in ocupando sobre reserva:
--    - Reserva ID 6: habitación 2, del 2025-12-28 al 2026-01-02
--    - Crear estadía asociada a esta reserva

-- D. Conflictos de reservas:
--    - Habitación 3: tiene reservas IDs 11 (20-25 ene) y 12 (22-27 ene)
--    - Intentar ocupar del 21 al 26 de enero

-- ================================================================================
