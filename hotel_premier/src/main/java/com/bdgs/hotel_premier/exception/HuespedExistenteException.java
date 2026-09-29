package com.bdgs.hotel_premier.exception;

/**
 * La excepción <code>HuespedExistenteException</code> se lanza cuando se intenta
 * registrar un huesped cuyo tipo y número de documento ya existe en el sistema.
 * Se utiliza, por ejemplo, al intentar almacenar en el archivo CSV un huesped que
 * ya fue almacenado.
 *
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo  
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */

public class HuespedExistenteException extends RuntimeException {
    /**
     * Este constructor llama al constructor de la clase padre y le pasa el mensaje incluido como parametro
     * @param message mensaje que describe la causa de la excepcion
     */
    public HuespedExistenteException(String message) {
        super(message);
    }
}
