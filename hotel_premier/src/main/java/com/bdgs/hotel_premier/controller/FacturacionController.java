package com.bdgs.hotel_premier.controller;

import com.bdgs.hotel_premier.dto.*;
import com.bdgs.hotel_premier.service.FacturacionFacade;

import jakarta.validation.Valid;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja los endpoints del proceso de facturación.
 * 
 * Flujo completo de facturación:
 * 1. GET /facturacion/estadia/huespedes → Obtiene grilla de huéspedes (Paso 5)
 * 2. POST /facturacion/estadia/preview → Previsualiza datos de facturación (Paso 6)
 * 3. POST /facturacion → Crea la factura definitiva (Paso 7-8)
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@RestController
public class FacturacionController {
    private final FacturacionFacade facturacionService;

    public FacturacionController(FacturacionFacade facturacionService) {
        this.facturacionService = facturacionService;
    }

    /**
     * Endpoint para obtener la lista de huéspedes de una estadía.
     * Corresponde al Paso 5 del flujo (Grilla de selección de responsable de pago).
     * 
     * @param consulta Query params con idHabitacion y fechaFin
     * @return EstadiaHuespedesDto con datos de estadía y lista de huéspedes
     * 
     * Ejemplo de uso:
     * GET /facturacion/estadia/huespedes?idHabitacion=101&fechaFin=2025-12-20
     */
    @GetMapping("/facturacion/estadia/huespedes")
    public ResponseEntity<EstadiaHuespedesDto> obtenerHuespedesEstadia(
            @Valid EstadiaConsultaDto consulta) {
        EstadiaHuespedesDto resultado = facturacionService.obtenerHuespedesEstadia(consulta);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Endpoint para previsualizar los datos de facturación con el responsable seleccionado.
     * Corresponde al Paso 6 del flujo (Items pendientes a facturar).
     * 
     * Calcula montos, tipo de factura y valida mayoría de edad del responsable.
     * 
     * IMPORTANTE: Si el responsable es persona jurídica (CUIT), DEBE estar previamente
     * registrada en el sistema. No se crean responsables en el preview.
     * 
     * @param estadiaId ID de la estadía a facturar
     * @param responsable Datos del responsable de pago (huésped o CUIT de persona jurídica existente)
     * @return FacturacionPreviewDto con preview de montos y tipo de factura
     * @throws NoSuchElementException si no se encuentra la persona jurídica
     * 
     * Ejemplo de uso:
     * POST /facturacion/estadia/123/preview
     * Body: { "huespedId": 456 } o { "cuit": "30-12345678-9" }
     */
    @PostMapping("/facturacion/estadia/{estadiaId}/preview")
    public ResponseEntity<FacturacionPreviewDto> previsualizarFacturacion(
            @PathVariable Long estadiaId,
            @Valid @RequestBody ResponsablePagoFacturaDto responsable) {
        FacturacionPreviewDto preview = facturacionService.previsualizarFacturacion(
                estadiaId, responsable);
        return ResponseEntity.ok(preview);
    }

    /**
     * Endpoint para crear la factura definitiva con los items seleccionados.
     * Corresponde a los Pasos 7-8 del flujo (Selección de items y Factura generada).
     * 
     * Crea o recupera el responsable de pago, genera la factura y la persiste.
     * 
     * @param solicitud Datos de la factura: estadía, responsable e items seleccionados
     * @return FacturaResultadoDto con datos de la factura creada
     * 
     * Ejemplo de uso con huésped:
     * POST /facturacion
     * Body: {
     *   "estadiaId": 123,
     *   "responsablePago": { "huespedId": 456 },
     *   "facturarEstadia": true,
     *   "facturarConsumos": true
     * }
     * 
     * Ejemplo de uso con persona jurídica existente:
     * POST /facturacion
     * Body: {
     *   "estadiaId": 123,
     *   "responsablePago": { "cuit": "30-12345678-9" },
     *   "facturarEstadia": true,
     *   "facturarConsumos": false
     * }
     */
    @PostMapping("/facturacion")
    public ResponseEntity<FacturaResultadoDto> crearFactura(
            @Valid @RequestBody FacturaSolicitudDto solicitud) {
        FacturaResultadoDto factura = facturacionService.crearFactura(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }
}
