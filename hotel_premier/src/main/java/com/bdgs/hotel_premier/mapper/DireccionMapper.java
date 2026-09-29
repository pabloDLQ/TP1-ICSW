package com.bdgs.hotel_premier.mapper;
import org.springframework.stereotype.Component;

import com.bdgs.hotel_premier.dto.DireccionDto;
import com.bdgs.hotel_premier.entity.Direccion;

/**
 * Mapper para convertir entre entidades {@link Direccion} y DTOs relacionados.
 * 
 * <p>Proporciona métodos para transformar objetos de dominio a DTOs de transferencia
 * y viceversa, facilitando la separación entre capas de la aplicación.</p>
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Component
public class DireccionMapper {

    public Direccion toEntity(DireccionDto dto) {
        if (dto == null) return null;
        return Direccion.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .nroDepartamento(dto.getNroDepartamento())
                .piso(dto.getPiso())
                .codigoPostal(dto.getCodigoPostal())
                .localidad(dto.getLocalidad())
                .provincia(dto.getProvincia())
                .pais(dto.getPais())
                .build();
    }

    public DireccionDto toDto(Direccion entity) {
        if (entity == null) return null;
        return DireccionDto.builder()
                .calle(entity.getCalle())
                .numero(entity.getNumero())
                .nroDepartamento(entity.getNroDepartamento())
                .piso(entity.getPiso())
                .codigoPostal(entity.getCodigoPostal())
                .localidad(entity.getLocalidad())
                .provincia(entity.getProvincia())
                .pais(entity.getPais())
                .build();
    }

}
