<div align="center">

# 🛍️ CRUD STORE API

### Una API REST robusta, segura y profesional para la gestión de inventarios

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.10-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth0-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.11-FF6F00?style=for-the-badge&logo=java&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

<br>

![Build](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)
![Coverage](https://img.shields.io/badge/coverage-60%25-yellow?style=flat-square)
![Version](https://img.shields.io/badge/version-1.0.0-blue?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)

</div>

📋 Tabla de Contenido

🎯 Descripción General

✨ Características Principales

🏗️ Arquitectura del Proyecto

💻 Tecnologías Utilizadas

🔐 Modelo de Seguridad (RBAC)

📍 API Endpoints

📂 Estructura de Archivos

🗄️ Base de Datos

⚙️ Instalación y Ejecución

🧪 Tests y Cobertura de Código

🚀 Despliegue con Docker

🚨 Manejo de Excepciones

📚 Documentación de la API (Swagger)

🏗️ Patrones de Diseño Implementados

🛣️ Roadmap / Plan Futuro

👥 Autor

📄 Licencia







<div align="center">
  
🎯 **Descripción General**

</div>

CRUD Store API es una aplicación backend desarrollada con Spring Boot que simula un sistema de gestión de inventario para una tienda real. Proporciona una API REST completa para administrar un catálogo de productos organizados en categorías.

El proyecto ha sido diseñado pensando en la producción real, implementando un modelo de seguridad avanzado basado en JWT con un sistema granular de roles y permisos (RBAC), y siguiendo las mejores prácticas de la industria: arquitectura limpia, pruebas automatizadas, contenerización con Docker y documentación interactiva.

¿Qué problema resuelve?
Ofrece una solución backend lista para integrarse con cualquier frontend (Angular, React, Vue, app móvil), permitiendo gestionar de forma segura y eficiente productos, categorías y usuarios.

Público objetivo: Desarrolladores full-stack, equipos de desarrollo, empresas que necesiten un backend robusto para su e-commerce o sistema de inventario, y reclutadores que buscan código de alta calidad.

<div align="center">


✨ **Características Principales**

| Funcionalidad                       | Estado    | Descripción                                                                                     |
|------------------------------------|-----------|-------------------------------------------------------------------------------------------------|
| ✅ CRUD de Productos                 | Completo  | Gestión completa de productos con soft delete (borrado lógico).                                 |
| ✅ CRUD de Categorías                | Completo  | Organización de productos por categorías con validación de unicidad.                            |
| ✅ Autenticación JWT                 | Completo  | Sistema seguro basado en tokens con Auth0 (HMAC256).                                            |
| ✅ Roles y Permisos (RBAC)           | Completo  | Control de acceso granular con permisos READ, CREATE, UPDATE, DELETE.                           |
| ✅ Seguridad por Endpoints           | Completo  | Protección de rutas basada en autoridades en SecurityConfig.                                    |
| ✅ Validaciones                      | Completo  | Validación en capas de DTO y entidades con Bean Validation.                                     |
| ✅ Manejo Global de Excepciones      | Completo  | Traducción de errores a códigos HTTP estándar con `@RestControllerAdvice`.                      |
| ✅ CORS Configurado                  | Completo  | Listo para comunicación con frontend en `localhost:4200` (Angular).                             |
| ✅ Pruebas Unitarias y de Slice      | Completo  | Tests para servicios y controladores con MockMvc y JUnit 5.                                     |
| ✅ Cobertura de Código con JaCoCo    | Completo  | Configuración con mínimo del 60% de cobertura en línea.                                         |
| ✅ Documentación OpenAPI (Swagger)   | Completo  | Documentación interactiva de la API disponible en `/swagger-ui.html`.                            |
| ✅ Dockerización Completa            | Completo  | `Dockerfile` y `docker-compose.yml` para levantar app + PostgreSQL.                              |
| ✅ Inicialización Automática de Datos| Completo  | `DataInitializer` que crea usuarios, roles y permisos por defecto al arrancar.                  |

</div>


<div align="center">
  
🏗️ **Arquitectura del Proyecto** 
  
</div>

El proyecto sigue una Arquitectura Hexagonal (también conocida como Puertos y Adaptadores) y está organizada en módulos funcionales, asegurando un bajo acoplamiento y alta mantenibilidad.

~~~
┌─────────────────────────────────────────────────────────────┐
│                       🌐 API LAYER                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Controllers  │  │     DTOs     │  │  Validation  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
├─────────────────────────────────────────────────────────────┤
│                    ⚙️ APPLICATION LAYER                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Services   │  │   Use Cases  │  │  Exceptions  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
├─────────────────────────────────────────────────────────────┤
│                      📦 DOMAIN LAYER                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Entities   │  │    Enums     │  │  Repository  │      │
│  │              │  │              │  │  Interfaces  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
├─────────────────────────────────────────────────────────────┤
│                 🔧 INFRASTRUCTURE LAYER                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Repositories │  │   Security   │  │  JPA/Hibernate│      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
~~~

### 🧱 Explicación de las Capas

El proyecto está estructurado siguiendo los principios de **Arquitectura Hexagonal**, separando claramente responsabilidades:

---

🔹 **API Layer**  
Recibe las peticiones HTTP, valida los DTOs de entrada y delega la ejecución en los servicios correspondientes.

🔹 **Application Layer**  
Contiene la lógica de negocio y los casos de uso. Orquesta las operaciones entre dominio e infraestructura.

🔹 **Domain Layer**  
Es el corazón del negocio. Define entidades, enums y reglas de negocio puras.  
No tiene dependencias externas.

🔹 **Infrastructure Layer**  
Implementa los detalles técnicos: repositorios JPA, configuración de seguridad, filtros JWT, persistencia y adaptadores externos.

---






