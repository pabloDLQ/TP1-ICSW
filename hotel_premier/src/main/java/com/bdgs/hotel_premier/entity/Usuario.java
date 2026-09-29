package com.bdgs.hotel_premier.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un usuario del sistema hotelero.
 * 
 * <p>Contiene las credenciales de acceso al sistema, con nombre de usuario
 * y contraseña. En una implementación completa, la contraseña debería
 * estar encriptada.</p>
 * 
 * <p><strong>Nota:</strong> Esta clase forma parte del modelo de dominio
 * completo pero no está persistida en la base de datos actual.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {
    private String nombre;
    private String contrasena;
}
