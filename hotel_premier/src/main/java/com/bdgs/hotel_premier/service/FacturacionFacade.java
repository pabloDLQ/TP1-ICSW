package com.bdgs.hotel_premier.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdgs.hotel_premier.dto.*;
import com.bdgs.hotel_premier.entity.*;
import com.bdgs.hotel_premier.enums.EstadoFactura;
import com.bdgs.hotel_premier.enums.PosicionIva;
import com.bdgs.hotel_premier.enums.TipoFactura;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Servicio Facade que coordina el proceso completo de facturación de estadías.
 * 
 * <p>Este servicio implementa el patrón Facade, proporcionando una interfaz simplificada
 * para el complejo proceso de facturación que involucra múltiples servicios y entidades.</p>
 * 
 * <p>Actúa como orquestador coordinando las operaciones entre:</p>
 * <ul>
 *   <li>EstadiaService: Gestión de estadías y sus datos</li>
 *   <li>HuespedService: Gestión de huéspedes</li>
 *   <li>PersonaFisicaService: Gestión de responsables de pago físicos</li>
 *   <li>PersonaJuridicaService: Gestión de responsables de pago jurídicos</li>
 *   <li>FacturaService: Persistencia de facturas</li>
 * </ul>
 * 
 * <p>Flujo de facturación:</p>
 * <ol>
 *   <li>Obtener huéspedes de la estadía para selección de responsable</li>
 *   <li>Previsualizar datos de facturación con cálculos de montos</li>
 *   <li>Crear factura definitiva con items seleccionados</li>
 * </ol>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class FacturacionFacade {
    private final EstadiaService estadiaService;
    private final HuespedService huespedService;
    private final PersonaFisicaService personaFisicaService;
    private final PersonaJuridicaService personaJuridicaService;
    private final FacturaService facturaService;

    public FacturacionFacade(
            EstadiaService estadiaService,
            HuespedService huespedService,
            PersonaFisicaService personaFisicaService,
            PersonaJuridicaService personaJuridicaService,
            FacturaService facturaService) {
        this.estadiaService = estadiaService;
        this.huespedService = huespedService;
        this.personaFisicaService = personaFisicaService;
        this.personaJuridicaService = personaJuridicaService;
        this.facturaService = facturaService;
    }

    /**
     * Obtiene la lista de huéspedes de una estadía para mostrar en la grilla
     * de selección de responsable de pago (Paso 5 del flujo).
     * 
     * @param consulta Contiene ID de habitación y fecha fin de estadía
     * @return EstadiaHuespedesDto con datos de la estadía y sus huéspedes
     * @throws NoSuchElementException si no se encuentra la estadía
     */
    public EstadiaHuespedesDto obtenerHuespedesEstadia(EstadiaConsultaDto consulta) {
        Estadia estadia = estadiaService.obtenerEstadiaPorIdHabYFechaFin(consulta);
        
        List<HuespedFacturacionDto> huespedesDto = estadia.getHuespedes().stream()
                .map(this::convertirAHuespedFacturacionDto)
                .collect(Collectors.toList());
        
        return EstadiaHuespedesDto.builder()
                .estadiaId(estadia.getId())
                .habitacionId(estadia.getHabitacion().getId())
                .codigoHabitacion(estadia.getHabitacion().getCodigo())
                .fechaInicio(estadia.getFechaInicio())
                .fechaFin(estadia.getFechaFin())
                .huespedes(huespedesDto)
                .build();
    }

    /**
     * Genera una previsualización de la factura con el responsable de pago seleccionado.
     * Calcula montos, tipo de factura y muestra items disponibles (Paso 6 del flujo).
     * 
     * @param estadiaId ID de la estadía a facturar
     * @param responsableSolicitud Datos del responsable de pago seleccionado
     * @return FacturacionPreviewDto con preview de la factura
     * @throws NoSuchElementException si no se encuentra la estadía o el responsable
     * @throws IllegalArgumentException si el responsable es menor de edad
     */
    public FacturacionPreviewDto previsualizarFacturacion(
            Long estadiaId, 
            ResponsablePagoFacturaDto responsableSolicitud) {
        
        // Obtener estadía por su ID
        Estadia estadia = estadiaService.obtenerEstadiaPorId(estadiaId);
        
        // Obtener o validar responsable de pago
        ResponsablePagoInfo responsableInfo = obtenerResponsablePagoInfo(responsableSolicitud);
        
        // Validar mayoría de edad si es persona física
        if (responsableInfo.esPersonaFisica && responsableInfo.fechaNacimiento != null) {
            validarMayoriaDeEdad(responsableInfo.fechaNacimiento);
        }
        
        // Calcular montos
        Float valorEstadia = calcularValorEstadia(estadia);
        Float consumoTotal = calcularConsumoTotal(estadia);
        Float totalAPagar = valorEstadia + consumoTotal;
        
        // Determinar tipo de factura según posición IVA
        TipoFactura tipoFactura = determinarTipoFactura(responsableInfo.posicionIva);
        
        // Obtener consumos individuales
        List<GastoAdicionalDto> consumos = estadia.getGastosAdicionales().stream()
                .map(this::convertirAGastoAdicionalDto)
                .collect(Collectors.toList());
        
        return FacturacionPreviewDto.builder()
                .responsablePagoNombre(responsableInfo.nombre)
                .responsablePagoApellido(responsableInfo.apellido)
                .responsablePagoCuit(responsableInfo.cuit)
                .valorEstadia(valorEstadia)
                .consumoTotalHabitacion(consumoTotal)
                .consumosIndividuales(consumos)
                .totalAPagar(totalAPagar)
                .tipoFactura(tipoFactura)
                .build();
    }

    /**
     * Crea la factura definitiva con los items seleccionados por el usuario (Paso 7-8 del flujo).
     * Persiste el responsable de pago si no existe y genera la factura asociada a la estadía.
     * 
     * @param solicitud Datos de la factura a crear
     * @return FacturaResultadoDto con datos de la factura creada
     * @throws NoSuchElementException si no se encuentra la estadía
     * @throws IllegalArgumentException si no se selecciona ningún item o el responsable es menor
     */
    @Transactional
    public FacturaResultadoDto crearFactura(FacturaSolicitudDto solicitud) {
        // Validar que se seleccionó al menos un item
        if (!solicitud.getFacturarEstadia() && !solicitud.getFacturarConsumos()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un item para facturar");
        }
        
        // Obtener estadía por su ID
        Estadia estadia = estadiaService.obtenerEstadiaPorId(solicitud.getEstadiaId());
        
        // Obtener o crear responsable de pago
        ResponsablePago responsable = obtenerOCrearResponsablePago(solicitud.getResponsablePago());
        ResponsablePagoInfo responsableInfo = obtenerResponsablePagoInfoParaFactura(solicitud.getResponsablePago());
        
        // Calcular monto total según items seleccionados
        Float montoTotal = 0f;
        Float valorEstadia = 0f;
        Float consumoTotal = 0f;
        
        if (solicitud.getFacturarEstadia()) {
            valorEstadia = calcularValorEstadia(estadia);
            montoTotal += valorEstadia;
        }
        
        if (solicitud.getFacturarConsumos()) {
            consumoTotal = calcularConsumoTotal(estadia);
            montoTotal += consumoTotal;
        }
        
        // Determinar tipo de factura
        TipoFactura tipoFactura = determinarTipoFactura(responsableInfo.posicionIva);
        
        // Crear y persistir factura
        Factura factura = Factura.builder()
                .montoFactura(montoTotal)
                .tipo(tipoFactura)
                .estado(EstadoFactura.NO_PAGADA)
                .fechaEmision(LocalDate.now())
                .responsablePago(responsable)
                .estadia(estadia)
                .build();
        
        factura = facturaService.guardar(factura);
        
        // Obtener consumos individuales para incluir en el resultado
        List<GastoAdicionalDto> consumos = estadia.getGastosAdicionales().stream()
                .map(this::convertirAGastoAdicionalDto)
                .collect(Collectors.toList());
        
        return FacturaResultadoDto.builder()
                .id(factura.getId())
                .montoFactura(factura.getMontoFactura())
                .tipo(factura.getTipo())
                .estado(factura.getEstado())
                .fechaEmision(factura.getFechaEmision())
                .responsablePagoNombre(responsableInfo.nombre + " " + responsableInfo.apellido)
                .responsablePagoCuit(responsableInfo.cuit)
                .valorEstadia(valorEstadia)
                .consumoTotal(consumoTotal)
                .consumosIndividuales(consumos)
                .totalFacturado(montoTotal)
                .build();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Convierte una entidad Huesped a DTO para facturación incluyendo edad calculada.
     */
    private HuespedFacturacionDto convertirAHuespedFacturacionDto(Huesped huesped) {
        Integer edad = huesped.getFechaNacimiento() != null 
                ? Period.between(huesped.getFechaNacimiento(), LocalDate.now()).getYears()
                : null;
        
        return HuespedFacturacionDto.builder()
                .id(huesped.getId())
                .apellido(huesped.getApellido())
                .nombre(huesped.getNombre())
                .tipoDocumento(huesped.getTipoDocumento())
                .numeroDocumento(huesped.getNumeroDocumento())
                .fechaNacimiento(huesped.getFechaNacimiento())
                .edad(edad)
                .posicionIva(huesped.getPosicionIva())
                .cuit(huesped.getCuit())
                .build();
    }

    /**
     * Convierte una entidad GastoAdicional a DTO.
     */
    private GastoAdicionalDto convertirAGastoAdicionalDto(GastoAdicional gasto) {
        return GastoAdicionalDto.builder()
                .precio(gasto.getPrecio())
                .descripcion(gasto.getDescripcion())
                .tipoGastoAdicional(gasto.getTipoGastoAdicional())
                .build();
    }

    /**
     * Calcula el valor total de la estadía (noches × precio por noche).
     */
    private Float calcularValorEstadia(Estadia estadia) {
        long dias = ChronoUnit.DAYS.between(estadia.getFechaInicio(), estadia.getFechaFin());
        Float precioPorNoche = estadia.getHabitacion().getTipoHabitacion().getCostoPorNoche();
        return dias * precioPorNoche;
    }

    /**
     * Calcula la suma de todos los gastos adicionales de la estadía.
     */
    private Float calcularConsumoTotal(Estadia estadia) {
        return estadia.getGastosAdicionales().stream()
                .map(GastoAdicional::getPrecio)
                .reduce(0f, Float::sum);
    }

    /**
     * Determina el tipo de factura según la posición IVA del responsable.
     * 
     * Reglas:
     * - RESPONSABLE_INSCRIPTO → Factura A
     * - EXENTO → Factura A
     * - CONSUMIDOR_FINAL → Factura B
     */
    private TipoFactura determinarTipoFactura(PosicionIva posicionIva) {
        return switch (posicionIva) {
            case RESPONSABLE_INSCRIPTO, EXENTO -> TipoFactura.A;
            case CONSUMIDOR_FINAL -> TipoFactura.B;
        };
    }

    /**
     * Valida que la persona sea mayor de edad (18 años).
     * 
     * @throws IllegalArgumentException si es menor de edad
     */
    private void validarMayoriaDeEdad(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 18) {
            throw new IllegalArgumentException(
                    "El responsable de pago debe ser mayor de edad. Edad actual: " + edad);
        }
    }

    /**
     * Obtiene información del responsable de pago desde la solicitud PARA PREVIEW.
     * Puede ser un huésped existente o una persona jurídica YA REGISTRADA.
     * 
     * IMPORTANTE: Para persona jurídica, DEBE existir en la base de datos.
     * No se crean responsables en el preview.
     */
    private ResponsablePagoInfo obtenerResponsablePagoInfo(ResponsablePagoFacturaDto solicitud) {
        if (solicitud.getHuespedId() != null) {
            // Responsable es un huésped - delegamos la búsqueda al HuespedService
            Huesped huesped = huespedService.obtenerPorId(solicitud.getHuespedId());
            
            return new ResponsablePagoInfo(
                    true,
                    huesped.getNombre(),
                    huesped.getApellido(),
                    huesped.getCuit(),
                    huesped.getPosicionIva(),
                    huesped.getFechaNacimiento()
            );
        } else if (solicitud.getCuit() != null) {
            // Responsable es persona jurídica - DEBE EXISTIR para preview
            PersonaJuridica personaJuridica = personaJuridicaService.obtenerPorCuit(solicitud.getCuit());
            
            return new ResponsablePagoInfo(
                    false,
                    personaJuridica.getRazonSocial(),
                    "",
                    personaJuridica.getCuit(),
                    personaJuridica.getPosicionIva(),
                    null
            );
        } else {
            throw new IllegalArgumentException(
                    "Debe especificar un huésped o un CUIT para el responsable de pago");
        }
    }

    /**
     * Obtiene información del responsable de pago PARA FACTURAR.
     * Solo busca datos existentes (igual que en el preview).
     */
    private ResponsablePagoInfo obtenerResponsablePagoInfoParaFactura(ResponsablePagoFacturaDto solicitud) {
        if (solicitud.getHuespedId() != null) {
            // Responsable es un huésped - delegamos la búsqueda al HuespedService
            Huesped huesped = huespedService.obtenerPorId(solicitud.getHuespedId());
            
            return new ResponsablePagoInfo(
                    true,
                    huesped.getNombre(),
                    huesped.getApellido(),
                    huesped.getCuit(),
                    huesped.getPosicionIva(),
                    huesped.getFechaNacimiento()
            );
        } else if (solicitud.getCuit() != null) {
            // Responsable es persona jurídica - DEBE EXISTIR
            PersonaJuridica personaJuridica = personaJuridicaService.obtenerPorCuit(solicitud.getCuit());
            
            return new ResponsablePagoInfo(
                    false,
                    personaJuridica.getRazonSocial(),
                    "",
                    personaJuridica.getCuit(),
                    personaJuridica.getPosicionIva(),
                    null
            );
        } else {
            throw new IllegalArgumentException(
                    "Debe especificar un huésped o un CUIT para el responsable de pago");
        }
    }

    /**
     * Obtiene o crea el responsable de pago según la solicitud AL MOMENTO DE FACTURAR.
     * 
     * LÓGICA:
     * - Si es huésped: busca PersonaFisica existente o CREA una nueva asociada al huésped
     * - Si es CUIT: busca PersonaJuridica existente (DEBE existir, no se crea)
     * 
     * Si la PersonaJuridica no existe, lanza excepción (ya debió validarse en preview).
     */
    private ResponsablePago obtenerOCrearResponsablePago(ResponsablePagoFacturaDto solicitud) {
        if (solicitud.getHuespedId() != null) {
            // Buscar PersonaFisica existente o CREAR nueva - delegamos al PersonaFisicaService
            Huesped huesped = huespedService.obtenerPorId(solicitud.getHuespedId());
            
            validarMayoriaDeEdad(huesped.getFechaNacimiento());
            
            return personaFisicaService.obtenerOCrear(huesped);
        } else if (solicitud.getCuit() != null) {
            // Solo BUSCAR PersonaJuridica existente (NO crear) - delegamos al PersonaJuridicaService
            return personaJuridicaService.obtenerPorCuit(solicitud.getCuit());
        } else {
            throw new IllegalArgumentException(
                    "Debe especificar un huésped o un CUIT para el responsable de pago");
        }
    }

    /**
     * Clase auxiliar para encapsular información del responsable de pago.
     */
    private static class ResponsablePagoInfo {
        final boolean esPersonaFisica;
        final String nombre;
        final String apellido;
        final String cuit;
        final PosicionIva posicionIva;
        final LocalDate fechaNacimiento;

        ResponsablePagoInfo(boolean esPersonaFisica, String nombre, String apellido, 
                           String cuit, PosicionIva posicionIva, LocalDate fechaNacimiento) {
            this.esPersonaFisica = esPersonaFisica;
            this.nombre = nombre;
            this.apellido = apellido;
            this.cuit = cuit;
            this.posicionIva = posicionIva;
            this.fechaNacimiento = fechaNacimiento;
        }
    }
}
