package com.bdgs.hotel_premier.enums;;

/**
 * El enum <code>EstadoReserva</code> representa los posibles estados
 * de una reserva dentro del sistema.<p>
 * - <code>NO_CONFIRMADA</code>: la reserva fue creada pero aún no confirmada.<p>
 * - <code>CONFIRMADA</code>: la reserva ha sido confirmada.<p>
 * - <code>FINALIZADA</code>: la reserva ya se cumplió y finalizó.<p>
 * - <code>CANCELADA</code>: la reserva fue cancelada antes de su cumplimiento.
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo  
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */

public enum EstadoReserva {
    NO_CONFIRMADA,
    CONFIRMADA,
    FINALIZADA,
    CANCELADA
}
