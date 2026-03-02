package com.mglopez.crudstore.modules.producto.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglopez.crudstore.modules.producto.api.dtos.ProductoCreateDTO;
import com.mglopez.crudstore.modules.producto.api.dtos.ProductoResponseDTO;
import com.mglopez.crudstore.modules.producto.application.ProductoService;
import com.mglopez.crudstore.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // GET /api/productos
    @Test
    @DisplayName("GET productos - retorna lista cuando usuario tiene READ")
    @WithMockUser(authorities = "READ")
    void shouldReturnProductos() throws Exception {

        // GIVEN
        List<ProductoResponseDTO> response = List.of(
                new ProductoResponseDTO(
                        1L, "Laptop", "Gaming",
                        new BigDecimal("1500"),
                        true, 10L, "Tecnologia",
                        LocalDateTime.now()
                )
        );

        when(productoService.listarProductosActivos())
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productoService).listarProductosActivos();
    }

    // GET /api/productos/{id}
    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturnProductoById() throws Exception {

        // GIVEN
        ProductoResponseDTO response =
                new ProductoResponseDTO(
                        1L, "Laptop", "Gaming",
                        new BigDecimal("1500"),
                        true, 10L, "Tecnologia",
                        LocalDateTime.now()
                );

        when(productoService.obtenerPorId(1L))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturn404_whenProductoNotFound() throws Exception {

        // GIVEN
        when(productoService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        // WHEN + THEN
        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }


    // GET /api/productos/categoria/{id}
    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturnProductosByCategoria() throws Exception {

        // GIVEN
        List<ProductoResponseDTO> response = List.of(
                new ProductoResponseDTO(
                        1L, "Mouse", "RGB",
                        new BigDecimal("50"),
                        true, 10L, "Tecnologia",
                        LocalDateTime.now()
                )
        );

        when(productoService.listarProductosPorCategoria(10L))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/productos/categoria/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaNombre")
                        .value("Tecnologia"));
    }


    // POST /api/productos
    // ============================================================

    @Test
    @WithMockUser(authorities = "CREATE")
    void shouldCreateProducto() throws Exception {

        // GIVEN
        ProductoCreateDTO request =
                new ProductoCreateDTO(
                        "Laptop", "Gaming",
                        new BigDecimal("1500"),
                        10L
                );

        ProductoResponseDTO response =
                new ProductoResponseDTO(
                        1L, "Laptop", "Gaming",
                        new BigDecimal("1500"),
                        true, 10L, "Tecnologia",
                        LocalDateTime.now()
                );

        when(productoService.crearProducto(any()))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(post("/api/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    @WithMockUser(authorities = "CREATE")
    void shouldReturn400_whenInvalidBody() throws Exception {

        // GIVEN
        String invalidJson = "{}";

        // WHEN + THEN
        mockMvc.perform(post("/api/productos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturn403_whenUserWithoutCreatePermission() throws Exception {

        // WHEN + THEN
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Test"}
                                """))
                .andExpect(status().isForbidden());
    }

    // PUT /api/productos/{id}
    @Test
    @WithMockUser(authorities = "UPDATE")
    void shouldUpdateProducto() throws Exception {

        // GIVEN
        ProductoCreateDTO request =
                new ProductoCreateDTO(
                        "Laptop Pro",
                        "Updated",
                        new BigDecimal("2000"),
                        10L
                );

        ProductoResponseDTO response =
                new ProductoResponseDTO(
                        1L, "Laptop Pro", "Updated",
                        new BigDecimal("2000"),
                        true, 10L, "Tecnologia",
                        LocalDateTime.now()
                );

        when(productoService.actualizarProducto(eq(1L), any()))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(put("/api/productos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"));
    }

    // DELETE /api/productos/{id}
    // ============================================================

    @Test
    @WithMockUser(authorities = "DELETE")
    void shouldDeleteProducto() throws Exception {

        // GIVEN
        doNothing().when(productoService).eliminarProducto(1L);

        // WHEN + THEN
        mockMvc.perform(delete("/api/productos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturn403_whenUserWithoutDeletePermission() throws Exception {

        // WHEN + THEN
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isForbidden());
    }

}
