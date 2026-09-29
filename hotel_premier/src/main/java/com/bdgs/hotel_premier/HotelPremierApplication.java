package com.bdgs.hotel_premier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Hotel Premier.
 * 
 * <p>Punto de entrada de la aplicación Spring Boot que gestiona el sistema
 * de administración hotelera, incluyendo reservas, estadías, huéspedes y facturación.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@SpringBootApplication
public class HotelPremierApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelPremierApplication.class, args);
    }

}