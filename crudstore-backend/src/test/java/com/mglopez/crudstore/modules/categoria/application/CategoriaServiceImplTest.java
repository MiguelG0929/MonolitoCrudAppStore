package com.mglopez.crudstore.modules.categoria.application;

import com.mglopez.crudstore.modules.categoria.api.dtos.CategoriaCreateDTO;
import com.mglopez.crudstore.modules.categoria.application.impl.CategoriaServiceImpl;
import com.mglopez.crudstore.modules.categoria.domain.CategoriaEntity;
import com.mglopez.crudstore.modules.categoria.infrastructure.CategoriaRepository;
import com.mglopez.crudstore.shared.exception.BadRequestException;
import com.mglopez.crudstore.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    //----crearCategoria----
    @Test
    void crearCategoria_deberiCrearCorrectamente()
    {
        //GIVEN
        CategoriaCreateDTO dto = new CategoriaCreateDTO("Electrodomesticos", "Productos esenciales para el hogar");

        when(categoriaRepository.existsByNombre("Electrodomesticos"))
                .thenReturn(false);

        CategoriaEntity saved = CategoriaEntity.builder()
                .id(1L)
                .nombre("Electrodomesticos")
                .descripcion("Productos esenciales para el hogar")
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(categoriaRepository.save(any())).thenReturn(saved);

        // ---- WHEN ----
        var result = categoriaService.crearCategoria(dto);

        // ---- THEN ----
        assertEquals("Electrodomesticos", result.nombre());
        verify(categoriaRepository).save(any());

    }

    @Test
    void crearCategoria_deberiaLanzarExceptionSiExiste() {

        // ---------- GIVEN ----------
        CategoriaCreateDTO dto =
                new CategoriaCreateDTO("Electrodomesticos", "Productos esenciales para el hogar");

        when(categoriaRepository.existsByNombre("Electrodomesticos"))
                .thenReturn(true);

        // ---------- WHEN & THEN ----------
        assertThrows(
                BadRequestException.class,
                () -> categoriaService.crearCategoria(dto)
        );

        verify(categoriaRepository, never()).save(any());
    }

    // ---------- listarCategoriasActivas ----------

    @Test
    void listarCategoriasActivas_deberiaRetornarLista() {

        // ---------- GIVEN ----------
        CategoriaEntity entity = CategoriaEntity.builder()
                .id(1L)
                .nombre("Electrodomesticos")
                .descripcion("Productos esenciales para el hogar")
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(categoriaRepository.findByActivaTrue())
                .thenReturn(List.of(entity));

        // ---------- WHEN ----------
        var result = categoriaService.listarCategoriasActivas();

        // ---------- THEN ----------
        assertEquals(1, result.size());
        assertEquals("Electrodomesticos", result.get(0).nombre());
    }

    // ---------- obtenerPorId ----------

    @Test
    void obtenerPorId_deberiaRetornarCategoria() {

        // ---------- GIVEN ----------
        CategoriaEntity entity = CategoriaEntity.builder()
                .id(1L)
                .nombre("Electrodomesticos")
                .descripcion("Productos esenciales para el hogar")
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        // ---------- WHEN ----------
        var result = categoriaService.obtenerPorId(1L);

        // ---------- THEN ----------
        assertEquals("Electrodomesticos", result.nombre());
    }

    @Test
    void obtenerPorId_deberiaLanzarExceptionSiNoExiste() {

        // ---------- GIVEN ----------
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.obtenerPorId(1L)
        );
    }

    @Test
    void actualizarCategoria_deberiaActualizarCorrectamente() {

        // ---------- GIVEN ----------
        CategoriaEntity entity = CategoriaEntity.builder()
                .id(1L)
                .nombre("Electrodomesticos")
                .descripcion("Productos esenciales para el hogar")
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        CategoriaCreateDTO dto =
                new CategoriaCreateDTO("ElectrodomesticosHogar", "Productos esenciales para el hogar");

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(categoriaRepository.existsByNombre("ElectrodomesticosHogar"))
                .thenReturn(false);

        when(categoriaRepository.save(any())).thenReturn(entity);

        // ---------- WHEN ----------
        var result = categoriaService.actualizarCategoria(1L, dto);

        // ---------- THEN ----------
        assertEquals("ElectrodomesticosHogar", result.nombre());
    }


    @Test
    void actualizarCategoria_deberiaFallarSiNombreDuplicado() {

        // ---------- GIVEN ----------
        CategoriaEntity entity = CategoriaEntity.builder()
                .id(1L)
                .nombre("Electrodomesticos")
                .build();

        CategoriaCreateDTO dto =
                new CategoriaCreateDTO("ElectrodomesticosHogar", "Productos esenciales para el hogar");

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(categoriaRepository.existsByNombre("ElectrodomesticosHogar"))
                .thenReturn(true);

        // ---------- WHEN & THEN ----------
        assertThrows(
                BadRequestException.class,
                () -> categoriaService.actualizarCategoria(1L, dto)
        );
    }


    // ---------- eliminarCategoria ----------

    @Test
    void eliminarCategoria_deberiaDesactivarCategoria() {

        // ---------- GIVEN ----------
        CategoriaEntity entity = CategoriaEntity.builder()
                .id(1L)
                .activa(true)
                .build();

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        // ---------- WHEN ----------
        categoriaService.eliminarCategoria(1L);

        // ---------- THEN ----------
        assertFalse(entity.getActiva());
        verify(categoriaRepository).save(entity);
    }

    @Test
    void eliminarCategoria_deberiaLanzarExceptionSiNoExiste() {

        // ---------- GIVEN ----------
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.eliminarCategoria(1L)
        );
    }


}

