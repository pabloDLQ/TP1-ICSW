package com.bdgs.hotel_premier.enums;

/**
 * El enum <code>PosicionIva</code> representa las posibles posiciones
 * frente al IVA de una persona o empresa (entidades) dentro del sistema.<p>
 * - <code>CONSUMIDOR_FINAL</code>: indica que la persona es consumidor final.<p>
 * - <code>RESPONSABLE_INSCRIPTO</code>: indica que la persona o empresa
 *   está inscripta como responsable ante el IVA.<p>
 * - <code>EXENTO</code>: indica que la persona o empresa está exenta de IVA.
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo  
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */

public enum PosicionIva {
    CONSUMIDOR_FINAL,
    RESPONSABLE_INSCRIPTO,
    EXENTO
}
