package com.mglopez.crudstore.modules.auth.application;

import com.mglopez.crudstore.modules.auth.api.dtos.AuthCreateUserDTO;
import com.mglopez.crudstore.modules.auth.api.dtos.AuthLoginRequestDTO;
import com.mglopez.crudstore.modules.auth.api.dtos.AuthResponseDTO;
import com.mglopez.crudstore.modules.auth.domain.RoleEntity;
import com.mglopez.crudstore.modules.auth.domain.RoleEnum;
import com.mglopez.crudstore.modules.auth.domain.UserEntity;
import com.mglopez.crudstore.modules.auth.infrastructure.repositories.RoleRepository;
import com.mglopez.crudstore.modules.auth.infrastructure.repositories.UserRepository;
import com.mglopez.crudstore.modules.auth.infrastructure.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ------------------------------------------------------------------
 * SERVICIO DE USUARIOS Y AUTENTICACIÓN (UserDetailsServiceImpl)
 * ------------------------------------------------------------------
 * Implementación de UserDetailsService para integración con Spring Security.
 * Gestiona:
 *  - Carga de usuarios para autenticación
 *  - Autenticación con usuario y contraseña
 *  - Generación de JWT
 *  - Creación de usuarios con roles y permisos
 *
 * ------------------------------------------------------------------
 * ROL ARQUITECTÓNICO
 * ------------------------------------------------------------------
 * Capa   : Application / Service
 * Módulo : auth
 *
 * - Integración de la capa de persistencia (repositorios de usuarios y roles)
 *   con Spring Security.
 * - Permite que la API REST use JWT para autenticación stateless.
 * - Controla validaciones críticas: existencia de usuario, roles válidos,
 *   contraseñas correctas y permisos asociados.
 *
 * ------------------------------------------------------------------
 * MÉTODOS PRINCIPALES
 * ------------------------------------------------------------------
 * 1. loadUserByUsername(String username)
 *      - Implementación obligatoria de UserDetailsService.
 *      - Carga UserDetails para Spring Security.
 *      - Convierte roles y permisos en authorities.
 *
 * 2. loginUser(AuthLoginRequestDTO authLoginRequestDTO)
 *      - Autentica al usuario con credenciales.
 *      - Genera token JWT si la autenticación es correcta.
 *      - Retorna AuthResponseDTO con token y mensaje de éxito.
 *
 * 3. authenticate(String username, String password)
 *      - Verifica que usuario exista y la contraseña sea correcta.
 *      - Lanza BadCredentialsException en caso de fallo.
 *      - Devuelve objeto Authentication para usar en SecurityContext.
 *
 * 4. createUser(AuthCreateUserDTO authCreateUserDTO)
 *      - Crea un nuevo usuario en la base de datos.
 *      - Valida roles proporcionados y genera contraseñas cifradas.
 *      - Genera authorities y token JWT para el usuario recién creado.
 *      - Retorna AuthResponseDTO con token y mensaje de éxito.
 *
 * ------------------------------------------------------------------
 * BUENAS PRÁCTICAS Y NOTAS
 * ------------------------------------------------------------------
 * - Usar PasswordEncoder para almacenar contraseñas de forma segura.
 * - Validar roles y permisos antes de persistir un usuario.
 * - Generar JWT de manera consistente usando JwtUtils.
 * - Mantener separation of concerns:
 *      - Repositorios solo persistencia
 *      - Servicio solo lógica de autenticación y autorización
 * - Manejar excepciones de manera explícita (BadCredentialsException,
 *   UsernameNotFoundException, IllegalArgumentException) para feedback claro.
 * - Convertir roles y permisos en authorities para Spring Security.
 * ------------------------------------------------------------------
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder; // Para cifrado y validación de contraseñas

    @Autowired
    private JwtUtils jwtUtils; // Utilidad para crear y validar JWT

    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios

    @Autowired
    private RoleRepository roleRepository; // Repositorio de roles

    /**
     * Carga un usuario por username para Spring Security
     *
     * @param username Nombre de usuario
     * @return UserDetails con roles y permisos (authorities)
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Convertir roles a authorities
        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));

        // Convertir permisos de roles a authorities
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorityList
        );
    }

    /**
     * Método de login: autentica usuario y genera JWT
     *
     * @param authLoginRequestDTO DTO con username y password
     * @return AuthResponseDTO con token JWT
     */
    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) {
        String username = authLoginRequestDTO.username();
        String password = authLoginRequestDTO.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponseDTO(username, "User logged successfully", accessToken, true);
    }

    /**
     * Autenticación explícita de usuario
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Authentication con authorities
     */
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    /**
     * Crea un nuevo usuario con roles y genera JWT
     *
     * @param authCreateUserDTO DTO con username, password y roles
     * @return AuthResponseDTO con token JWT
     */
    public AuthResponseDTO createUser(AuthCreateUserDTO authCreateUserDTO) {
        String username = authCreateUserDTO.username();
        String password = authCreateUserDTO.password();

        // Convertir roles de String -> RoleEnum
        List<RoleEnum> roleEnums = authCreateUserDTO.roleRequest()
                .roleListName()
                .stream()
                .map(role -> RoleEnum.valueOf(role.toUpperCase()))
                .toList();

        // Buscar roles reales en la base de datos
        Set<RoleEntity> roleEntitySet = roleRepository.findRoleEntitiesByRoleEnumIn(roleEnums)
                .stream()
                .collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("Los roles especificados no existen");
        }

        // Crear entidad de usuario
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        // Generar authorities
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
        );
        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        // Crear token JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userCreated.getUsername(),
                null,
                authorityList
        );
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponseDTO(
                userCreated.getUsername(),
                "User created successfully",
                accessToken,
                true
        );
    }
}
