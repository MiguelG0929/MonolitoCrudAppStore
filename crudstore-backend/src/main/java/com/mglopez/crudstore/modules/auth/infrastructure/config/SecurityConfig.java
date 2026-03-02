package com.mglopez.crudstore.modules.auth.infrastructure.config;

import com.mglopez.crudstore.modules.auth.infrastructure.security.filter.JWTokenValidator;
import com.mglopez.crudstore.modules.auth.infrastructure.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * ------------------------------------------------------------------
 * CONFIGURACIÓN DE SEGURIDAD (SecurityConfig)
 * ------------------------------------------------------------------
 * Clase central de configuración de Spring Security.
 *
 * - Define cómo se autentican y autorizan los usuarios en la aplicación.
 * - Configura JWT, roles, permisos y filtros de seguridad.
 * - Establece políticas de sesión y seguridad de endpoints.
 *
 * ------------------------------------------------------------------
 * ROL ARQUITECTÓNICO
 * ------------------------------------------------------------------
 * Capa   : Infrastructure / Security
 * Módulo : auth
 *
 * - Coordina la integración de Spring Security con el sistema de usuarios,
 *   roles y permisos.
 * - Configura filtros JWT y autorización basada en permisos (Authority).
 * - Aplica políticas de seguridad a nivel de HTTP y endpoints.
 *
 * ------------------------------------------------------------------
 * CONFIGURACIÓN PRINCIPAL
 * ------------------------------------------------------------------
 * 1. CSRF deshabilitado (por ser una API REST sin sesiones de navegador).
 * 2. HTTP Basic habilitado como fallback (opcional, puede eliminarse).
 * 3. Política de sesión: STATELESS para usar JWT y no mantener sesiones.
 * 4. Autorización de endpoints basada en permisos:
 *      - POST /auth/** -> permitido a todos (registro y login)
 *      - GET /api/categorias/** -> requiere "READ"
 *      - POST /api/categorias/** -> requiere "CREATE"
 *      - PUT /api/categorias/** -> requiere "UPDATE"
 *      - DELETE /api/categorias/** -> requiere "DELETE"
 *      - Otros endpoints -> autenticación requerida
 * 5. Filtro JWT (JWTokenValidator) agregado antes del filtro básico de Spring Security
 *
 * ------------------------------------------------------------------
 * BEANS DE SEGURIDAD
 * ------------------------------------------------------------------
 * - AuthenticationManager:
 *      - Gestiona la autenticación con Spring Security.
 * - AuthenticationProvider:
 *      - Configurado con UserDetailsService y PasswordEncoder.
 *      - Permite validar credenciales de usuario.
 * - PasswordEncoder:
 *      - BCryptPasswordEncoder para almacenar contraseñas seguras.
 *
 * ------------------------------------------------------------------
 * BUENAS PRÁCTICAS
 * ------------------------------------------------------------------
 * - Usar permisos como Authority en lugar de roles para granularidad fina.
 * - Mantener la política STATELESS para APIs REST.
 * - Validar JWT en un filtro antes de que llegue a los controladores.
 * - Nunca exponer contraseñas ni información sensible en respuestas.
 * ------------------------------------------------------------------
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils; // Utilidad para generar y validar JWT

    /**
     * Configuración principal de la cadena de filtros de Spring Security
     *
     * @param httpSecurity HttpSecurity
     * @return SecurityFilterChain configurado
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Deshabilita CSRF para APIs REST
                .csrf(csrf -> csrf.disable())

                //Desactivar auth clásica
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())

                //SIN sesiones
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuración de autorización de endpoints
                .authorizeHttpRequests(http -> {

                    http.requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                    ).permitAll();

                    // Endpoints de autenticación abiertos
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

                    // Endpoints de categorías protegidos por permisos
                    http.requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAuthority("READ");
                    http.requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAuthority("CREATE");
                    http.requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAuthority("DELETE");

                    // Todos los demás endpoints requieren autenticación
                    http.anyRequest().authenticated();
                })
                // Agrega filtro JWT antes del BasicAuthenticationFilter
                .addFilterBefore(new JWTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean para AuthenticationManager de Spring Security
     *
     * @param authenticationConfiguration configuración de autenticación
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean para AuthenticationProvider
     *
     * - Usa UserDetailsService para cargar usuarios.
     * - Configura PasswordEncoder para validar contraseñas.
     *
     * @param userDetailsService implementación de UserDetailsService
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Bean para PasswordEncoder
     *
     * - BCryptPasswordEncoder para almacenar contraseñas seguras.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
