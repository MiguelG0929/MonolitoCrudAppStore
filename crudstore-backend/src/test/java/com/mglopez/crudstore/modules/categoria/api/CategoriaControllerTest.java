package com.mglopez.crudstore.modules.categoria.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglopez.crudstore.modules.categoria.api.dtos.CategoriaCreateDTO;
import com.mglopez.crudstore.modules.categoria.api.dtos.CategoriaResponseDTO;
import com.mglopez.crudstore.modules.categoria.application.CategoriaService;
import com.mglopez.crudstore.shared.exception.BadRequestException;
import com.mglopez.crudstore.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ================================================================
 * TESTS DEL CONTROLLER CategoriaController
 * ================================================================
 *
 * Se testea:
 * - Contrato HTTP
 * - Seguridad (Spring Security)
 * - Serialización JSON
 * - Validaciones
 * - Status codes REST
 *
 * NO se testea:
 * - lógica de negocio
 * - JWT real
 * - repositorios
 *
 * ================================================================
 */

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   //Nueva practica Mock deprecated
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    // GET /api/categorias
    @Test
    @DisplayName("GET categorias - debe retornar lista cuando usuario tiene READ")
    @WithMockUser(authorities = "READ")
    void shouldReturnCategorias_whenUserHasReadPermission() throws Exception {

        // GIVEN
        List<CategoriaResponseDTO> response =
                List.of(new CategoriaResponseDTO(
                        1L,
                        "Tecnologia",
                        "Categoria tecnologia",
                        true,
                        LocalDateTime.now()
                ));

        when(categoriaService.listarCategoriasActivas())
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Tecnologia"));
        verify(categoriaService, times(1)).listarCategoriasActivas();
    }

    // GET /api/categorias/{id}
    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturnCategoriaById() throws Exception {

        // GIVEN
        CategoriaResponseDTO response =
                new CategoriaResponseDTO(
                        1L,
                        "Tecnologia",
                        "Categoria tecnologia",
                        true,
                        LocalDateTime.now()
                );

        when(categoriaService.obtenerPorId(1L))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }

    @Test
    @DisplayName("GET categorias/{id} - debe retornar 404 cuando el servicio lanza ResourceNotFound")
    @WithMockUser(authorities = "READ")
    void shouldReturn404_whenCategoriaNotFound() throws Exception {

        // GIVEN
        Long idNoExistente = 99L;
        when(categoriaService.obtenerPorId(idNoExistente))
                .thenThrow(new ResourceNotFoundException("Categoría no encontrada con ID " + idNoExistente));

        // WHEN + THEN
        mockMvc.perform(get("/api/categorias/{id}", idNoExistente))
                .andExpect(status().isNotFound()); // Verificamos que el controlador devuelve 404
    }


    // POST /api/categorias/create
    @Test
    @WithMockUser(authorities = "CREATE")
    void shouldCreateCategoria() throws Exception {

        // GIVEN
        CategoriaCreateDTO request =
                new CategoriaCreateDTO
                        ("Tecnologia", "Categoria tecnologia");

        CategoriaResponseDTO response =
                new CategoriaResponseDTO(
                        1L,
                        "Tecnologia",
                        "Categoria tecnologia",
                        true,
                        LocalDateTime.now()
                );

        when(categoriaService.crearCategoria(any()))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(post("/api/categorias/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }


    @Test
    @DisplayName("POST categorias/create - debe retornar 400 cuando el servicio lanza BadRequest por nombre duplicado")
    @WithMockUser(authorities = "CREATE")
    void shouldReturn400_whenServiceThrowsBadRequestForDuplicateName() throws Exception {

        // GIVEN
        CategoriaCreateDTO request = new CategoriaCreateDTO("Duplicada", "Desc");
        when(categoriaService.crearCategoria(any()))
                .thenThrow(new BadRequestException("La categoría ya existe: Duplicada"));

        // WHEN + THEN
        mockMvc.perform(post("/api/categorias/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Verificamos que el controlador traduce la excepción a 400
    }

    @Test
    @WithMockUser(authorities = "CREATE")
    void shouldReturn400_whenInvalidBody() throws Exception {

        // GIVEN
        String invalidJson = "{}";

        // WHEN + THEN
        mockMvc.perform(post("/api/categorias/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturn403_whenUserWithoutCreatePermission() throws Exception {

        mockMvc.perform(post("/api/categorias/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"nombre":"Test"}
                            """))
                .andExpect(status().isForbidden());
    }

    // PUT /api/categorias/{id}
    @Test
    @WithMockUser(authorities = "UPDATE")
    void shouldUpdateCategoria() throws Exception {

        // GIVEN
        CategoriaCreateDTO request =
                new CategoriaCreateDTO("Electronica", "Categoria Electronica");

        CategoriaResponseDTO response =
                new CategoriaResponseDTO(
                        1L,
                        "Electronica",
                        "Categoria Electronica",
                        true,
                        LocalDateTime.now()
                );

        when(categoriaService.actualizarCategoria(eq(1L), any()))
                .thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(put("/api/categorias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Electronica"));
    }

    // DELETE /api/categorias/{id}
    @Test
    @WithMockUser(authorities = "DELETE")
    void shouldDeleteCategoria() throws Exception {

        // GIVEN
        doNothing().when(categoriaService).eliminarCategoria(1L);

        // WHEN + THEN
        mockMvc.perform(delete("/api/categorias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void shouldReturn403_whenUserWithoutDeletePermission() throws Exception {

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isForbidden());
    }

}
