-- Ejemplos de peticiones HTTP para probar el CU15 "Ocupar Habitación"

-- ========================================
-- PASO 1: Consultar disponibilidad
-- ========================================
-- GET /habitaciones/disponibilidad?inicio=2024-01-15&fin=2024-01-20
-- Esto mostrará el estado de cada habitación por día

-- ========================================
-- PASO 2: Consultar conflictos (opcional)
-- ========================================
-- GET /habitaciones/conflictos-reservas?habitacionId=3&inicio=2024-01-15&fin=2024-01-20
-- Respuesta esperada para habitación 3:
-- [
--   {
--     "reservaId": 1,
--     "titular": "González, Juan", 
--     "telefono": "1122334455",
--     "diasConflicto": ["2024-01-16", "2024-01-17"]
--   }
-- ]

-- ========================================
-- PASO 3: Casos de prueba para check-in
-- ========================================

-- CASO 1: Check-in exitoso (habitación disponible)
-- POST /estadias
-- Content-Type: application/json
-- {
--   "huespedPrincipal": {
--     "tipoDocumento": "DNI",
--     "numeroDocumento": "30111222"
--   },
--   "acompanantes": [
--     {
--       "tipoDocumento": "DNI",
--       "numeroDocumento": "28999111"
--     }
--   ],
--   "items": [
--     {
--       "habitacionId": 1,
--       "fechaInicio": "2024-01-15",
--       "fechaFin": "2024-01-17",
--       "ocuparSobreReserva": false,
--       "reservaIdAsociada": null
--     }
--   ]
-- }

-- CASO 2: Check-in ocupando sobre reserva (con permiso)
-- POST /estadias
-- Content-Type: application/json
-- {
--   "huespedPrincipal": {
--     "tipoDocumento": "PASAPORTE",
--     "numeroDocumento": "AB123456"
--   },
--   "acompanantes": [],
--   "items": [
--     {
--       "habitacionId": 3,
--       "fechaInicio": "2024-01-16",
--       "fechaFin": "2024-01-18",
--       "ocuparSobreReserva": true,
--       "reservaIdAsociada": 1
--     }
--   ]
-- }

-- CASO 3: Check-in múltiple (varias habitaciones)
-- POST /estadias
-- Content-Type: application/json
-- {
--   "huespedPrincipal": {
--     "tipoDocumento": "DNI",
--     "numeroDocumento": "31222333"
--   },
--   "acompanantes": [
--     {
--       "tipoDocumento": "DNI",
--       "numeroDocumento": "29888777"
--     },
--     {
--       "tipoDocumento": "DNI",
--       "numeroDocumento": "30000111"
--     }
--   ],
--   "items": [
--     {
--       "habitacionId": 4,
--       "fechaInicio": "2024-01-19",
--       "fechaFin": "2024-01-21",
--       "ocuparSobreReserva": false,
--       "reservaIdAsociada": null
--     },
--     {
--       "habitacionId": 5,
--       "fechaInicio": "2024-01-22",
--       "fechaFin": "2024-01-24",
--       "ocuparSobreReserva": false,
--       "reservaIdAsociada": null
--     }
--   ]
-- }

-- ========================================
-- CASOS DE ERROR ESPERADOS
-- ========================================

-- ERROR 400: Huésped no existe
-- POST /estadias con tipoDocumento: "DNI", numeroDocumento: "99999999"

-- ERROR 409: Habitación no disponible (mantenimiento)
-- POST /estadias intentando ocupar habitación 6 del 2024-01-14 al 2024-01-16

-- ERROR 409: Habitación ocupada
-- POST /estadias intentando ocupar habitación 2 del 2024-01-12 al 2024-01-15

-- ERROR 409: Reserva sin permiso
-- POST /estadias con habitación 3, fechas 2024-01-16 a 2024-01-18, ocuparSobreReserva: false

-- ========================================
-- COMANDOS CURL PARA TESTING
-- ========================================

-- 1. Consultar disponibilidad:
-- curl -X GET "http://localhost:8080/habitaciones/disponibilidad?inicio=2024-01-15&fin=2024-01-20"

-- 2. Consultar conflictos:
-- curl -X GET "http://localhost:8080/habitaciones/conflictos-reservas?habitacionId=3&inicio=2024-01-15&fin=2024-01-20"

-- 3. Check-in exitoso:
-- curl -X POST http://localhost:8080/estadias \
--   -H "Content-Type: application/json" \
--   -d '{
--     "huespedPrincipal": {"tipoDocumento": "DNI", "numeroDocumento": "30111222"},
--     "acompanantes": [{"tipoDocumento": "DNI", "numeroDocumento": "28999111"}],
--     "items": [{
--       "habitacionId": 1,
--       "fechaInicio": "2024-01-15",
--       "fechaFin": "2024-01-17",
--       "ocuparSobreReserva": false,
--       "reservaIdAsociada": null
--     }]
--   }'