package com.bdgs.hotel_premier.service;

import com.bdgs.hotel_premier.dto.*;
import com.bdgs.hotel_premier.entity.Habitacion;
import com.bdgs.hotel_premier.entity.Reserva;
import com.bdgs.hotel_premier.entity.TipoHabitacion;
import com.bdgs.hotel_premier.enums.EstadoReserva;
import com.bdgs.hotel_premier.repository.ReservaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    // -------------------------
    // agregarReserva
    // -------------------------
    @Test
    void agregarReserva_ok() {
        Reserva reserva = new Reserva();

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva result = reservaService.agregarReserva(reserva);

        assertNotNull(result);
        assertEquals(reserva, result);
        verify(reservaRepository).save(reserva);
    }

    // -------------------------
    // obtenerReservaPorId
    // -------------------------
    @Test
    void obtenerReservaPorId_existe() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        Reserva result = reservaService.obtenerReservaPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerReservaPorId_noExiste() {
        when(reservaRepository.findById(1L))
                .thenReturn(Optional.empty());

        Reserva result = reservaService.obtenerReservaPorId(1L);

        assertNull(result);
    }

    // -------------------------
    // guardarReserva
    // -------------------------
    @Test
    void guardarReserva_ok() {
        Reserva reserva = new Reserva();

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva result = reservaService.guardarReserva(reserva);

        assertEquals(reserva, result);
        verify(reservaRepository).save(reserva);
    }

    // -------------------------
    // obtenerReservasPorRango
    // -------------------------
    @Test
    void obtenerReservasPorRango_ok() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        List<Reserva> reservas = List.of(new Reserva());

        when(reservaRepository.findReservasPorRango(inicio, fin))
                .thenReturn(reservas);

        List<Reserva> result =
                reservaService.obtenerReservasPorRango(inicio, fin);

        assertEquals(reservas, result);
        verify(reservaRepository).findReservasPorRango(inicio, fin);
    }

    // -------------------------
    // buscarReservas
    // -------------------------
    @Test
    void buscarReservas_filtraCanceladas() {
        TipoHabitacion tipoHab = new TipoHabitacion();
        tipoHab.setDescripcion("Habitación Doble");
        
        Habitacion hab = new Habitacion();
        hab.setId(1L);
        hab.setTipoHabitacion(tipoHab);

        Reserva activa = Reserva.builder()
                .nombre("Juan")
                .apellido("Perez")
                .estado(EstadoReserva.CONFIRMADA)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(2))
                .habitacion(hab)
                .build();

        Reserva cancelada = Reserva.builder()
                .estado(EstadoReserva.CANCELADA)
                .build();

        when(reservaRepository.findAll(ArgumentMatchers.<Example<Reserva>>any()))
        .thenReturn(List.of(activa, cancelada));


        ReservaFiltroDto filtro = ReservaFiltroDto.builder()
                .nombre("Juan")
                .build();

        List<ReservaInfoDto> result =
                reservaService.buscarReservas(filtro);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
    }

    // -------------------------
    // obtenerDiasReservadosPorHabitacion
    // -------------------------
    @Test
    void obtenerDiasReservadosPorHabitacion_soloConfirmadas() {
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 13);

        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Reserva confirmada = Reserva.builder()
                .habitacion(hab)
                .estado(EstadoReserva.CONFIRMADA)
                .fechaInicio(LocalDate.of(2025, 1, 10))
                .fechaFin(LocalDate.of(2025, 1, 11))
                .build();

        Reserva cancelada = Reserva.builder()
                .habitacion(hab)
                .estado(EstadoReserva.CANCELADA)
                .build();

        when(reservaRepository.findReservasPorRango(inicio, fin))
                .thenReturn(List.of(confirmada, cancelada));

        Map<Long, Set<LocalDate>> result =
                reservaService.obtenerDiasReservadosPorHabitacion(inicio, fin);

        assertEquals(
                Set.of(
                        LocalDate.of(2025, 1, 10),
                        LocalDate.of(2025, 1, 11)
                ),
                result.get(1L)
        );
    }

    // -------------------------
    // obtenerConflictosReservas
    // -------------------------
    @Test
    void obtenerConflictosReservas_ok() {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Reserva reserva = Reserva.builder()
                .id(10L)
                .nombre("Ana")
                .apellido("Gomez")
                .telefono("123")
                .habitacion(hab)
                .estado(EstadoReserva.CONFIRMADA)
                .fechaInicio(LocalDate.of(2025, 1, 10))
                .fechaFin(LocalDate.of(2025, 1, 12))
                .build();

        when(reservaRepository.findReservasPorRango(any(), any()))
                .thenReturn(List.of(reserva));

        List<ConflictoReservaDto> conflictos =
                reservaService.obtenerConflictosReservas(
                        1L,
                        LocalDate.of(2025, 1, 10),
                        LocalDate.of(2025, 1, 12)
                );

        assertEquals(1, conflictos.size());
        assertEquals(10L, conflictos.get(0).getReservaId());
        assertEquals("Gomez, Ana", conflictos.get(0).getTitular());
        assertFalse(conflictos.get(0).getDiasConflicto().isEmpty());
    }

    // -------------------------
    // cancelarReservas
    // -------------------------
    @Test
    void cancelarReservas_ok() {
        Habitacion hab = new Habitacion();
        hab.setId(1L);

        Reserva reserva = Reserva.builder()
                .nombre("Luis")
                .apellido("Diaz")
                .telefono("456")
                .habitacion(hab)
                .fechaInicio(LocalDate.of(2025, 1, 10))
                .fechaFin(LocalDate.of(2025, 1, 12))
                .estado(EstadoReserva.CONFIRMADA)
                .build();

        ReservaCancelarDto dto = ReservaCancelarDto.builder()
                .nombre("Luis")
                .apellido("Diaz")
                .fechaInicio(reserva.getFechaInicio())
                .fechaFin(reserva.getFechaFin())
                .idHabitacion(1L)
                .build();

        when(reservaRepository.findByNombreAndApellidoAndFechaInicioAndFechaFinAndHabitacionId(
                any(), any(), any(), any(), anyLong()
        )).thenReturn(reserva);

        when(reservaRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        List<ReservaDto> result =
                reservaService.cancelarReservas(List.of(dto));

        assertEquals(1, result.size());
        assertEquals("Luis", result.get(0).getNombre());
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }
}
