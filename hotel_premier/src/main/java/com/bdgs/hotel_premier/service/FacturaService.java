package com.bdgs.hotel_premier.service;

import org.springframework.stereotype.Service;

import com.bdgs.hotel_premier.entity.Factura;
import com.bdgs.hotel_premier.repository.FacturaRepository;

/**
 * Servicio que gestiona las operaciones de persistencia relacionadas con facturas.
 * 
 * <p>Este servicio actúa como capa de abstracción sobre el repositorio de facturas,
 * proporcionando métodos para guardar y consultar facturas en la base de datos.</p>
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Crear y persistir nuevas facturas</li>
 *   <li>Consultar facturas existentes</li>
 *   <li>Gestionar el ciclo de vida de las facturas en la BD</li>
 * </ul>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * Guarda una factura en la base de datos.
     * 
     * @param factura Factura a guardar
     * @return Factura guardada con ID generado
     */
    public Factura guardar(Factura factura) {
        return facturaRepository.save(factura);
    }
}
