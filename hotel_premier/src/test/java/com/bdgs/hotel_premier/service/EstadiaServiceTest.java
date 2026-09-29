package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.EstadiaConsultaDto;
import com.bdgs.hotel_premier.entity.Estadia;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.repository.EstadiaRepository;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadiaServiceTest {

    @Mock
    private EstadiaRepository estadiaRepository;

    @InjectMocks
    private EstadiaService estadiaService;

    // -------------------------------------------------
    // guardarEstadia
    // -------------------------------------------------
    @Test
    void guardarEstadia_ok() {
        Estadia estadia = new Estadia();

        when(estadiaRepository.save(estadia)).thenReturn(estadia);

        Estadia result = estadiaService.guardarEstadia(estadia);

        assertNotNull(result);
        assertEquals(estadia, result);
        verify(estadiaRepository, times(1)).save(estadia);
    }

    // -------------------------------------------------
    // obtenerEstadiasPorRango
    // -------------------------------------------------
    @Test
    void obtenerEstadiasPorRango_ok() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        List<Estadia> estadias = List.of(new Estadia());

        when(estadiaRepository.findEstadiasPorRango(inicio, fin))
                .thenReturn(estadias);

        List<Estadia> result =
                estadiaService.obtenerEstadiasPorRango(inicio, fin);

        assertEquals(estadias, result);
        verify(estadiaRepository).findEstadiasPorRango(inicio, fin);
    }

    // -------------------------------------------------
    // obtenerDiasOcupadosPorHabitacion
    // -------------------------------------------------
    @Test
    void obtenerDiasOcupadosPorHabitacion_unaEstadia() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 13);

        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setFechaInicio(LocalDate.of(2025, 1, 10));
        estadia.setFechaFin(LocalDate.of(2025, 1, 12));

        when(estadiaRepository.findEstadiasPorRango(inicio, fin))
                .thenReturn(List.of(estadia));

        Map<Long, Set<LocalDate>> result =
                estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, fin);

        assertEquals(
                Set.of(
                        LocalDate.of(2025, 1, 10),
                        LocalDate.of(2025, 1, 11),
                        LocalDate.of(2025, 1, 12)
                ),
                result.get(1L)
        );
    }

    @Test
    void obtenerDiasOcupadosPorHabitacion_variasEstadiasMismaHabitacion() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 20);

        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Estadia e1 = new Estadia();
        e1.setHabitacion(hab);
        e1.setFechaInicio(LocalDate.of(2025, 1, 10));
        e1.setFechaFin(LocalDate.of(2025, 1, 11));

        Estadia e2 = new Estadia();
        e2.setHabitacion(hab);
        e2.setFechaInicio(LocalDate.of(2025, 1, 15));
        e2.setFechaFin(LocalDate.of(2025, 1, 16));

        when(estadiaRepository.findEstadiasPorRango(inicio, fin))
                .thenReturn(List.of(e1, e2));

        Map<Long, Set<LocalDate>> result =
                estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, fin);

        assertEquals(4, result.get(1L).size());
    }

    @Test
    void obtenerDiasOcupadosPorHabitacion_variasHabitaciones() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 13);

        Habitacion h1 = new Habitacion();
        h1.setId(1L);
        Habitacion h2 = new Habitacion();
        h2.setId(2L);

        Estadia e1 = new Estadia();
        e1.setHabitacion(h1);
        e1.setFechaInicio(inicio);
        e1.setFechaFin(LocalDate.of(2025, 1, 11));

        Estadia e2 = new Estadia();
        e2.setHabitacion(h2);
        e2.setFechaInicio(inicio);
        e2.setFechaFin(LocalDate.of(2025, 1, 12));

        when(estadiaRepository.findEstadiasPorRango(inicio, fin))
                .thenReturn(List.of(e1, e2));

        Map<Long, Set<LocalDate>> result =
                estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, fin);

        assertEquals(2, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals(3, result.get(2L).size());
    }

    @Test
    void obtenerDiasOcupadosPorHabitacion_sinEstadias() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        when(estadiaRepository.findEstadiasPorRango(inicio, fin))
                .thenReturn(Collections.emptyList());

        Map<Long, Set<LocalDate>> result =
                estadiaService.obtenerDiasOcupadosPorHabitacion(inicio, fin);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------
    // Casos borde de fechas (fin exclusivo)
    // -------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "2025-01-10,2025-01-10,2025-01-10,2025-01-11,1", // un día
            "2025-01-01,2025-01-05,2025-01-10,2025-01-15,0", // fuera de rango
            "2025-01-05,2025-01-09,2025-01-10,2025-01-15,0"  // checkout antes
    })
    void obtenerDiasOcupadosPorHabitacion_casosBorde(
            LocalDate inicioEstadia,
            LocalDate finEstadia,
            LocalDate inicioConsulta,
            LocalDate finConsulta,
            int cantidadEsperada
    ) {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setFechaInicio(inicioEstadia);
        estadia.setFechaFin(finEstadia);

        when(estadiaRepository.findEstadiasPorRango(inicioConsulta, finConsulta))
                .thenReturn(List.of(estadia));

        Map<Long, Set<LocalDate>> result =
                estadiaService.obtenerDiasOcupadosPorHabitacion(inicioConsulta, finConsulta);

        assertEquals(cantidadEsperada, result.getOrDefault(1L, Set.of()).size());
    }

    // -------------------------------------------------
    // obtenerEstadiaPorIdHabYFechaFin
    // -------------------------------------------------
    @Test
    void obtenerEstadiaPorIdHabYFechaFin_ok() {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setFechaInicio(LocalDate.of(2025, 1, 10));
        estadia.setFechaFin(LocalDate.of(2025, 1, 12));

        EstadiaConsultaDto consulta =
                EstadiaConsultaDto.builder()
                        .idHabitacion(1L)
                        .fechaFin(LocalDate.of(2025, 1, 12))
                        .build();

        when(estadiaRepository.findByHabitacionIdAndFechaFin(1L, consulta.getFechaFin()))
                .thenReturn(Optional.of(estadia));

        Estadia estadia2 =
                estadiaService.obtenerEstadiaPorIdHabYFechaFin(consulta);

        assertEquals(1L, estadia2.getHabitacion().getId());
        assertEquals(estadia.getFechaInicio(), estadia2.getFechaInicio());
        assertEquals(estadia.getFechaFin(), estadia2.getFechaFin());
    }

    @Test
    void obtenerEstadiaPorIdHabYFechaFin_noExiste() {
        EstadiaConsultaDto consulta =
                EstadiaConsultaDto.builder()
                        .idHabitacion(1L)
                        .fechaFin(LocalDate.now())
                        .build();

        when(estadiaRepository.findByHabitacionIdAndFechaFin(anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> estadiaService.obtenerEstadiaPorIdHabYFechaFin(consulta)
        );
    }

    // -------------------------------------------------
    // obtenerEstadiaPorId
    // -------------------------------------------------
    @Test
    void obtenerEstadiaPorId_existe() {
        Estadia estadia = new Estadia();
        estadia.setId(1L);

        when(estadiaRepository.findById(1L))
                .thenReturn(Optional.of(estadia));

        Estadia result = estadiaService.obtenerEstadiaPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerEstadiaPorId_noExiste() {
        when(estadiaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> estadiaService.obtenerEstadiaPorId(99L)
        );
    }
}

