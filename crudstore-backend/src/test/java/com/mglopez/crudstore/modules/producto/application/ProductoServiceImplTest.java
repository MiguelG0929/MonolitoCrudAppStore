package com.mglopez.crudstore.modules.producto.application;

import com.mglopez.crudstore.modules.categoria.domain.CategoriaEntity;
import com.mglopez.crudstore.modules.categoria.infrastructure.CategoriaRepository;
import com.mglopez.crudstore.modules.producto.api.dtos.ProductoCreateDTO;
import com.mglopez.crudstore.modules.producto.application.impl.ProductoServiceImpl;
import com.mglopez.crudstore.modules.producto.domain.ProductoEntity;
import com.mglopez.crudstore.modules.producto.infrastructure.ProductoRepository;
import com.mglopez.crudstore.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;


    @Test
    void crearProducto_deberiaGuardarProductoCuandoCategoriaExiste() {

        // ---------- GIVEN ----------
        CategoriaEntity categoria = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        ProductoCreateDTO dto = new ProductoCreateDTO(
                "Laptop",
                "Gaming",
                new BigDecimal("1200"),
                1L
        );

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        ProductoEntity productoGuardado = ProductoEntity.builder()
                .id(10L)
                .name("Laptop")
                .descripcion("Gaming")
                .precio(new BigDecimal("1200"))
                .activo(true)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(productoRepository.save(any(ProductoEntity.class)))
                .thenReturn(productoGuardado);

        // ---------- WHEN ----------
        var resultado = productoService.crearProducto(dto);

        // ---------- THEN ----------

        // Validar respuesta
        assertNotNull(resultado);
        assertEquals("Laptop", resultado.name());
        assertTrue(resultado.activo());

        // Capturar el objeto realmente enviado al repository
        ArgumentCaptor<ProductoEntity> captor =
                ArgumentCaptor.forClass(ProductoEntity.class);

        verify(productoRepository).save(captor.capture());

        ProductoEntity productoEnviado = captor.getValue();

        assertEquals("Laptop", productoEnviado.getName());
        assertEquals("Gaming", productoEnviado.getDescripcion());
        assertEquals(new BigDecimal("1200"), productoEnviado.getPrecio());
        assertTrue(productoEnviado.getActivo());

        verify(categoriaRepository).findById(1L);
    }

    @Test
    void crearProducto_deberiaFallarSiCategoriaNoExiste() {

        // ---------- GIVEN ----------
        ProductoCreateDTO dto =
                new ProductoCreateDTO(
                        "Laptop",
                        "Gaming",
                        new BigDecimal("1200"),
                        1L
                );

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.crearProducto(dto)
        );

        verify(productoRepository, never()).save(any());
    }

    // ---------- obtenerPorId ----------

    @Test
    void obtenerPorId_deberiaRetornarProducto() {

        // ---------- GIVEN ----------
        CategoriaEntity categoria = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        ProductoEntity producto = ProductoEntity.builder()
                .id(10L)
                .name("Laptop")
                .descripcion("Gaming")
                .precio(new BigDecimal("1200"))
                .activo(true)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        // ---------- WHEN ----------
        var result = productoService.obtenerPorId(10L);

        // ---------- THEN ----------
        assertEquals("Laptop", result.name());
    }

    @Test
    void obtenerPorId_deberiaLanzarExceptionSiNoExiste() {

        // ---------- GIVEN ----------
        when(productoRepository.findById(10L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.obtenerPorId(10L)
        );
    }

    // ---------- listarProductosActivos ----------

    @Test
    void listarProductosActivos_deberiaRetornarLista() {

        // ---------- GIVEN ----------
        CategoriaEntity categoria = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        ProductoEntity producto = ProductoEntity.builder()
                .id(10L)
                .name("Laptop")
                .descripcion("Gaming")
                .precio(new BigDecimal("1200"))
                .activo(true)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(productoRepository.findByActivoTrue())
                .thenReturn(List.of(producto));

        // ---------- WHEN ----------
        var result = productoService.listarProductosActivos();

        // ---------- THEN ----------
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).name());
    }

    // ---------- listarProductosPorCategoria ----------

    @Test
    void listarProductosPorCategoria_deberiaRetornarLista() {

        // ---------- GIVEN ----------
        CategoriaEntity categoria = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        ProductoEntity producto = ProductoEntity.builder()
                .id(10L)
                .name("Laptop")
                .descripcion("Gaming")
                .precio(new BigDecimal("1200"))
                .activo(true)
                .categoria(categoria)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(productoRepository.findByCategoriaIdAndActivoTrue(1L))
                .thenReturn(List.of(producto));

        // ---------- WHEN ----------
        var result = productoService.listarProductosPorCategoria(1L);

        // ---------- THEN ----------
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).name());
    }

    // ---------- actualizarProducto ----------
    @Test
    void actualizarProducto_deberiaActualizarCorrectamente() {

        // ---------- GIVEN ----------
        CategoriaEntity categoria = CategoriaEntity.builder()
                .id(1L)
                .nombre("Tecnología")
                .build();

        ProductoEntity producto = ProductoEntity.builder()
                .id(10L)
                .name("Laptop")
                .descripcion("Gaming")
                .precio(new BigDecimal("100"))
                .activo(true)
                .categoria(categoria)
                .build();

        ProductoCreateDTO dto =
                new ProductoCreateDTO(
                        "Ordenador",
                        "Escritorio",
                        new BigDecimal("200"),
                        1L
                );

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(productoRepository.save(any())).thenReturn(producto);

        // ---------- WHEN ----------
        var result = productoService.actualizarProducto(10L, dto);

        // ---------- THEN ----------
        assertEquals("Ordenador", result.name());
        assertEquals(new BigDecimal("200"), result.precio());
    }

    @Test
    void actualizarProducto_deberiaFallarSiProductoNoExiste() {

        // ---------- GIVEN ----------
        ProductoCreateDTO dto =
                new ProductoCreateDTO(
                        "Ordenador",
                        "Escritorio",
                        new BigDecimal("200"),
                        1L
                );

        when(productoRepository.findById(10L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.actualizarProducto(10L, dto)
        );
    }

    // ---------- eliminarProducto ----------

    @Test
    void eliminarProducto_deberiaDesactivarProducto() {

        // ---------- GIVEN ----------
        ProductoEntity producto = ProductoEntity.builder()
                .id(10L)
                .activo(true)
                .build();

        when(productoRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        // ---------- WHEN ----------
        productoService.eliminarProducto(10L);

        // ---------- THEN ----------
        assertFalse(producto.getActivo());
        verify(productoRepository).save(producto);
    }

    @Test
    void eliminarProducto_deberiaLanzarExceptionSiNoExiste() {

        // ---------- GIVEN ----------
        when(productoRepository.findById(10L))
                .thenReturn(Optional.empty());

        // ---------- WHEN & THEN ----------
        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.eliminarProducto(10L)
        );
    }






}
