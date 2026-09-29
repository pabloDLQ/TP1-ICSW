package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.ReservaDto;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.entity.NoDisponibilidadHabitacion;
import com.bdgs.hotel_premier.repository.HabitacionRepository;
import com.bdgs.hotel_premier.repository.NoDisponibilidadHabitacionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private NoDisponibilidadHabitacionRepository noDisponibilidadRepo;

    @InjectMocks
    private HabitacionService habitacionService;

    // -------------------------------------------------
    // obtenerHabitacionPorId
    // -------------------------------------------------
    @Test
    void obtenerHabitacionPorId_existe() {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(hab));

        Habitacion result = habitacionService.obtenerHabitacionPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerHabitacionPorId_noExiste() {
        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.empty());

        Habitacion result = habitacionService.obtenerHabitacionPorId(1L);

        assertNull(result);
    }

    // -------------------------------------------------
    // obtenerHabitacionesParaReservas
    // -------------------------------------------------
    @Test
    void obtenerHabitacionesParaReservas_ok() {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        ReservaDto dto = ReservaDto.builder().idHabitacion(1L).build();

        when(habitacionRepository.findById(1L))
                .thenReturn(Optional.of(hab));

        List<Habitacion> result =
                habitacionService.obtenerHabitacionesParaReservas(List.of(dto));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void obtenerHabitacionesParaReservas_habitacionNoEncontrada() {
        ReservaDto dto = ReservaDto.builder().idHabitacion(99L).build();

        when(habitacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> habitacionService.obtenerHabitacionesParaReservas(List.of(dto))
        );
    }

    // -------------------------------------------------
    // obtenerTodasLasHabitaciones
    // -------------------------------------------------
    @Test
    void obtenerTodasLasHabitaciones_ok() {
        when(habitacionRepository.findAll())
                .thenReturn(List.of(new Habitacion(), new Habitacion()));

        List<Habitacion> result =
                habitacionService.obtenerTodasLasHabitaciones();

        assertEquals(2, result.size());
        verify(habitacionRepository).findAll();
    }

    // -------------------------------------------------
    // obtenerDiasNoDisponibles
    // -------------------------------------------------
    @Test
    void obtenerDiasNoDisponibles_unaHabitacion() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 13);

        Habitacion hab = new Habitacion();
        hab.setId(1L);

        NoDisponibilidadHabitacion nd = new NoDisponibilidadHabitacion();
        nd.setHabitacion(hab);
        nd.setFechaInicio(LocalDate.of(2025, 1, 10));
        nd.setFechaFin(LocalDate.of(2025, 1, 11));

        when(noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicio, fin))
                .thenReturn(List.of(nd));

        Set<LocalDate> result =
                habitacionService.obtenerDiasNoDisponibles(1L, inicio, fin);

        assertEquals(
                Set.of(
                        LocalDate.of(2025, 1, 10),
                        LocalDate.of(2025, 1, 11)
                ),
                result
        );
    }

    @Test
    void obtenerDiasNoDisponibles_filtraOtraHabitacion() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        Habitacion hab = new Habitacion();
        hab.setId(2L);

        NoDisponibilidadHabitacion nd = new NoDisponibilidadHabitacion();
        nd.setHabitacion(hab);
        nd.setFechaInicio(LocalDate.of(2025, 1, 10));
        nd.setFechaFin(LocalDate.of(2025, 1, 12));

        when(noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicio, fin))
                .thenReturn(List.of(nd));

        Set<LocalDate> result =
                habitacionService.obtenerDiasNoDisponibles(1L, inicio, fin);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------
    // obtenerDiasNoDisponiblesPorHabitacion
    // -------------------------------------------------
    @Test
    void obtenerDiasNoDisponiblesPorHabitacion_variasHabitaciones() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 13);

        Habitacion hab1 = new Habitacion();
        hab1.setId(1L);

        Habitacion hab2 = new Habitacion();
        hab2.setId(2L);

        NoDisponibilidadHabitacion nd1 = new NoDisponibilidadHabitacion();
        nd1.setHabitacion(hab1);
        nd1.setFechaInicio(LocalDate.of(2025, 1, 10));
        nd1.setFechaFin(LocalDate.of(2025, 1, 11));

        NoDisponibilidadHabitacion nd2 = new NoDisponibilidadHabitacion();
        nd2.setHabitacion(hab2);
        nd2.setFechaInicio(LocalDate.of(2025, 1, 11));
        nd2.setFechaFin(LocalDate.of(2025, 1, 12));

        when(noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicio, fin))
                .thenReturn(List.of(nd1, nd2));

        Map<Long, Set<LocalDate>> result =
                habitacionService.obtenerDiasNoDisponiblesPorHabitacion(inicio, fin);

        assertEquals(2, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals(2, result.get(2L).size());
    }

    // -------------------------------------------------
    // Casos borde con fechas (parametrizado)
    // -------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "2025-01-10,2025-01-10,2025-01-10,2025-01-11,1", // mismo día
            "2025-01-01,2025-01-05,2025-01-10,2025-01-15,0", // fuera de rango
            "2025-01-05,2025-01-09,2025-01-10,2025-01-15,0"  // termina antes
    })
    void obtenerDiasNoDisponibles_casosBorde(
            LocalDate inicioEntidad,
            LocalDate finEntidad,
            LocalDate inicioConsulta,
            LocalDate finConsulta,
            int cantidadEsperada
    ) {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        NoDisponibilidadHabitacion nd = new NoDisponibilidadHabitacion();
        nd.setHabitacion(hab);
        nd.setFechaInicio(inicioEntidad);
        nd.setFechaFin(finEntidad);

        when(noDisponibilidadRepo.findNoDisponibilidadesSuperpuestas(inicioConsulta, finConsulta))
                .thenReturn(List.of(nd));

        Set<LocalDate> result =
                habitacionService.obtenerDiasNoDisponibles(1L, inicioConsulta, finConsulta);

        assertEquals(cantidadEsperada, result.size());
    }
}

