package com.bdgs.hotel_premier.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.bdgs.hotel_premier.dto.DireccionDto;
import com.bdgs.hotel_premier.dto.HuespedDto;
import com.bdgs.hotel_premier.dto.HuespedEliminarDto;
import com.bdgs.hotel_premier.dto.HuespedFiltroDto;
import com.bdgs.hotel_premier.entity.Direccion;
import com.bdgs.hotel_premier.entity.Huesped;
import com.bdgs.hotel_premier.enums.TipoDocumento;
import com.bdgs.hotel_premier.exception.HuespedExistenteException;
import com.bdgs.hotel_premier.mapper.DireccionMapper;
import com.bdgs.hotel_premier.mapper.HuespedMapper;
import com.bdgs.hotel_premier.repository.DireccionRepository;
import com.bdgs.hotel_premier.repository.HuespedRepository;

/**
 * La clase {@code HuespedService} se encarga de administrar las operaciones
 * relacionadas con los huéspedes del sistema y sus direcciones. Permite consultar, agregar,
 * eliminar y buscar huéspedes, además de asociarles su dirección correspondiente.
 * 
 * @author Juan Cruz Ganis
 * @author Valentin Scilingo  
 * @author Marcos Ariel Bude
 * @author Pablo Dellacqua
 */
@Service
public class HuespedService {
    private final HuespedRepository huespedRepository;
    private final DireccionRepository direccionRepository;

    private final HuespedMapper huespedMapper;
    private final DireccionMapper direccionMapper;

    public HuespedService(HuespedRepository huespedRepository, HuespedMapper huespedMapper, DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }
    
    /**
     * Obtiene un huésped por tipo y número de documento.
     *
     * @param tipoDocumento tipo de documento del huésped.
     * @param numeroDocumento número de documento del huésped.
     * @return Optional con el huésped si existe, Optional.empty() si no existe.
     */
    public Optional<Huesped> obtenerHuespedPorDocumento(TipoDocumento tipoDocumento, String numeroDocumento) {
        return huespedRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    /**
     * Obtiene un huésped por su ID.
     * 
     * @param id ID del huésped
     * @return Huesped encontrado
     * @throws NoSuchElementException si no existe el huésped con ese ID
     */
    public Huesped obtenerPorId(Long id) {
        return huespedRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró el huésped con ID: " + id));
    }

    /**
     * Agrega un nuevo huésped al sistema si no existe previamente.
     *
        * @param huespedDto DTO del huésped a agregar.
        * @return DTO del huésped agregado (incluye dirección persistida).
     */
    public HuespedDto agregarHuesped(HuespedDto huespedDto) {
        validarHuespedExistente(huespedDto);

        Direccion direccion = obtenerOCrearDireccion(huespedDto.getDireccion());
        Huesped nuevoHuesped = huespedRepository.save(huespedMapper.toEntity(huespedDto, direccion));

        HuespedDto resultado = huespedMapper.toDto(nuevoHuesped, direccionMapper.toDto(direccion));
        
        return resultado;
    }

    /**
        * Elimina un huésped del sistema, identificado por tipo y número de documento.
        * Si la dirección asociada queda sin referencias por otros huéspedes, también se elimina.
        *
        * @param huespedDto DTO con los datos identificatorios del huésped a eliminar.
        * @return el mismo DTO recibido (solo confirmación lógica de la operación).
     */
    public HuespedDto eliminarHuesped(HuespedEliminarDto huespedDto) {
        // Eliminar por identificador lógico (tipo + número de documento)
        Huesped existente = huespedRepository
            .findByTipoDocumentoAndNumeroDocumento(huespedDto.getTipoDocumento(), huespedDto.getNumeroDocumento())
            .orElse(null);

        if (existente == null) {
            return null;
        }
        
        if(existente.getEstadias() != null && !existente.getEstadias().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el huésped porque tiene estadías asociadas.");
        }

        Direccion dir = existente.getDireccion();
        huespedRepository.delete(existente);
        eliminarDireccionSiHuerfana(dir);

        return huespedMapper.toDto(existente, direccionMapper.toDto(dir));
    }

    /**
     * Verifica si un huésped ya existe en el sistema a partir de su tipo y número de documento.
     * Si existe, lanza una excepción.
     *
        * @param huespedDto DTO con los datos identificatorios a verificar.
     * @throws HuespedExistenteException si ya existe un huésped con el mismo identificador.
     */
    public void validarHuespedExistente(HuespedDto huespedDto) {
        boolean existe = huespedRepository.existsByTipoDocumentoAndNumeroDocumento(
            huespedDto.getTipoDocumento(),
            huespedDto.getNumeroDocumento()
        );

        if (existe) {
            throw new HuespedExistenteException("El huésped ya existe.");
        }
    }

    /**
        * Busca huéspedes según los criterios indicados en un objeto {@link HuespedFiltroDto}.
        * Los campos nulos en el filtro se ignoran en el armado del Example.
     *
        * @param filtro un {@link HuespedFiltroDto} con los criterios de búsqueda (parciales y case-insensitive).
        * @return lista de huéspedes en forma de DTO que coinciden con los criterios.
     */
    public List<HuespedDto> buscarHuespedes(HuespedFiltroDto filtro) {

        // Crear el ejemplo de búsqueda
        Huesped ejemplo = huespedMapper.toEntity(filtro);

        // Normalizar strings vacíos a null solo para los campos de filtro válidos
        if (ejemplo.getApellido() != null && ejemplo.getApellido().trim().isEmpty()) {
            ejemplo.setApellido(null);
        }
        if (ejemplo.getNombre() != null && ejemplo.getNombre().trim().isEmpty()) {
            ejemplo.setNombre(null);
        }
        if (ejemplo.getNumeroDocumento() != null && ejemplo.getNumeroDocumento().trim().isEmpty()) {
            ejemplo.setNumeroDocumento(null);
        }
        
        // Limpiar campos que no aplican al filtro para evitar interferencia
        ejemplo.setCuit(null);
        ejemplo.setTelefono(null);
        ejemplo.setEmail(null);
        ejemplo.setOcupacion(null);
        ejemplo.setNacionalidad(null);
        ejemplo.setFechaNacimiento(null);
        ejemplo.setDireccion(null);

        // Seteamos manualmente a null para que el valor por defecto no interfiera en la búsqueda
        ejemplo.setPosicionIva(null);

        // Configurar el matcher: apellido y nombre "empieza con", resto exacto
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues() // ignora filtros no enviados (ahora incluye strings vacíos normalizados)
                .withIgnoreCase() // insensible a mayúsculas/minúsculas
                .withMatcher("apellido", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("nombre", ExampleMatcher.GenericPropertyMatchers.startsWith());

        // Crear el ejemplo de consulta
        Example<Huesped> example = Example.of(ejemplo, matcher);

        // Realizar la consulta
        List<Huesped> huespedes = huespedRepository.findAll(example);

        // Mapear los resultados a DTOs
        return huespedes.stream()
                .map(h -> {
                    return huespedMapper.toDto(h,direccionMapper.toDto(h.getDireccion()));
                })
                .toList();
    }

    /**
     * Modifica un huésped existente identificado por tipo y número de documento.
     * Actualiza todos los datos con los recibidos en el DTO, incluyendo la dirección
     * (se obtiene o crea la dirección persistida y se asigna).
     *
     * @param huespedDto DTO con todos los datos actualizados del huésped.
     * @return DTO del huésped ya actualizado.
     * @throws IllegalArgumentException si el huésped no existe.
     */
    public HuespedDto modificarHuesped(HuespedDto huespedDto) {
        // Este método asume que no se cambia el identificador (tipo+numero).
        // Si se cambian, usar la versión con parámetros originales.
        Huesped existente = huespedRepository
            .findByTipoDocumentoAndNumeroDocumento(huespedDto.getTipoDocumento(), huespedDto.getNumeroDocumento())
            .orElse(null);

        if (existente == null) {
            throw new IllegalArgumentException("El huésped a modificar no existe.");
        }

        Direccion direccionPersistida = obtenerOCrearDireccion(huespedDto.getDireccion());

        huespedMapper.updateEntity(existente, huespedDto);
        existente.setDireccion(direccionPersistida);

        Huesped guardado = huespedRepository.save(existente);
        return huespedMapper.toDto(guardado, direccionMapper.toDto(direccionPersistida));
    }

    /**
     * Modifica un huésped identificado por valores originales de documento, permitiendo cambiar
     * tipo y número de documento, validando colisión de unicidad.
     *
     * @param originalTipo tipo de documento actual del huésped a modificar.
     * @param originalNumero número de documento actual del huésped a modificar.
     * @param huespedDto DTO con los datos nuevos del huésped (puede incluir cambio de tipo/numero).
     * @return DTO del huésped actualizado.
     * @throws IllegalArgumentException si el huésped no existe.
     * @throws HuespedExistenteException si el nuevo tipo+numero ya está en uso por otro huésped.
     */
    public HuespedDto modificarHuesped(TipoDocumento originalTipo, String originalNumero, HuespedDto huespedDto) {
        Huesped existente = huespedRepository
            .findByTipoDocumentoAndNumeroDocumento(originalTipo, originalNumero)
            .orElse(null);

        if (existente == null) {
            throw new IllegalArgumentException("El huésped a modificar no existe.");
        }

        // Validar unicidad si cambia tipo/numero
        boolean cambiaIdentificador = 
            !(originalTipo == huespedDto.getTipoDocumento() && originalNumero.equals(huespedDto.getNumeroDocumento()));
        if (cambiaIdentificador) {
            validarHuespedExistente(huespedDto);
        }

        Direccion direccionPersistida = obtenerOCrearDireccion(huespedDto.getDireccion());
        huespedMapper.updateEntity(existente, huespedDto);
        existente.setDireccion(direccionPersistida);

        Huesped guardado = huespedRepository.save(existente);
        return huespedMapper.toDto(guardado, direccionMapper.toDto(direccionPersistida));
    }

    /**
     * Obtiene una dirección existente que coincida exactamente con todos los campos del DTO.
     * Si no existe, la crea y la persiste.
     *
     * @param direccion DTO con los datos completos de la dirección.
     * @return entidad {@link Direccion} ya persistida (existente o recién creada).
     */
    public Direccion obtenerOCrearDireccion(DireccionDto direccion) {
        Direccion dir = direccionRepository.findByCalleAndNumeroAndNroDepartamentoAndPisoAndCodigoPostalAndLocalidadAndProvinciaAndPais(
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getNroDepartamento(),
                direccion.getPiso(),
                direccion.getCodigoPostal(),
                direccion.getLocalidad(),
                direccion.getProvincia(),
                direccion.getPais()
        );
        
        if (dir != null) {
            return dir;
        } else {
            Direccion nuevaDireccion = direccionMapper.toEntity(direccion);
            return direccionRepository.save(nuevaDireccion);
        }
    }

    /**
     * Elimina la dirección recibida si no está referenciada por ningún huésped.
     * No realiza ninguna acción si la dirección es null o sigue teniendo referencias.
     *
     * @param direccion entidad dirección potencialmente huérfana.
     */
    private void eliminarDireccionSiHuerfana(Direccion direccion) {
        if (direccion == null) {
            return;
        }
        boolean sigueReferenciada = huespedRepository.existsByDireccion(direccion);
        if (!sigueReferenciada) {
            direccionRepository.delete(direccion);
        }
    }
}