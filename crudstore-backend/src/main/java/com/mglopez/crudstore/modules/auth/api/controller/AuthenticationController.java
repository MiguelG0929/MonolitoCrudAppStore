package com.mglopez.crudstore.modules.auth.api.controller;

import com.mglopez.crudstore.modules.auth.api.dtos.AuthCreateUserDTO;
import com.mglopez.crudstore.modules.auth.api.dtos.AuthLoginRequestDTO;
import com.mglopez.crudstore.modules.auth.api.dtos.AuthResponseDTO;
import com.mglopez.crudstore.modules.auth.application.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ------------------------------------------------------------------
 * CONTROLADOR DE AUTENTICACIÓN (AuthenticationController)
 * ------------------------------------------------------------------
 * Este controlador expone los endpoints de autenticación de usuarios:
 *  - Registro de nuevos usuarios (sign-up)
 *  - Login de usuarios existentes (log-in)
 *
 * Endpoints:
 *  1. POST /auth/sing-up
 *     - Recibe un AuthCreateUserDTO
 *     - Crea un nuevo usuario y devuelve un AuthResponseDTO con JWT
 *     - HTTP Status: 201 (CREATED)
 *
 *  2. POST /auth/log-in
 *     - Recibe un AuthLoginRequestDTO
 *     - Valida credenciales, genera JWT y devuelve AuthResponseDTO
 *     - HTTP Status: 200 (OK)
 *
 * Validación:
 *  - @Valid asegura que los DTOs recibidos cumplan con las restricciones
 *    definidas (ej. campos obligatorios, tamaño máximo, validación de roles)
 *
 * Uso interno:
 *  - Delegación total de la lógica a UserDetailsServiceImpl
 *  - JWT generado por JwtUtils
 *
 * ------------------------------------------------------------------
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Endpoint para registrar un nuevo usuario
     *
     * @param authCreateUser DTO con información de usuario y roles
     * @return AuthResponseDTO con JWT y mensaje de éxito
     */
    @PostMapping("/sing-up")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid AuthCreateUserDTO authCreateUser) {
        return new ResponseEntity<>(
                this.userDetailsService.createUser(authCreateUser),
                HttpStatus.CREATED
        );
    }

    /**
     * Endpoint para autenticar un usuario existente
     *
     * @param userRequest DTO con username y password
     * @return AuthResponseDTO con JWT y mensaje de éxito
     */
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(
                this.userDetailsService.loginUser(userRequest),
                HttpStatus.OK
        );
    }
}
