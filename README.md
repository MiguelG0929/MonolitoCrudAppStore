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

<div align="center">

## 💻 Tecnologías Utilizadas

| Categoría | Tecnología | Versión | Badge | Propósito |
|------------|------------|----------|--------|------------|
| Lenguaje | Java | 17 | ![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white) | Lenguaje principal |
| Framework | Spring Boot | 3.5.10 | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.10-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) | Framework base |
| Seguridad | Spring Security | 6.x | ![Security](https://img.shields.io/badge/Security-6.x-2E8B57?style=flat-square) | Autenticación y autorización |
| Seguridad | JWT (Auth0) | 4.5.0 | ![JWT](https://img.shields.io/badge/JWT-4.5.0-000000?style=flat-square) | Generación y validación de tokens |
| Persistencia | Spring Data JPA | 3.x | ![JPA](https://img.shields.io/badge/JPA-3.x-yellow?style=flat-square) | Mapeo objeto-relacional |
| Base de Datos | PostgreSQL | 16 | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=flat-square&logo=postgresql&logoColor=white) | Base de datos relacional |
| Build y Gestión | Maven | 3.8.x | ![Maven](https://img.shields.io/badge/Maven-3.8.x-C71A36?style=flat-square&logo=apache-maven&logoColor=white) | Gestión de dependencias y build |
| Pruebas | JUnit 5 | — | ![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5&logoColor=white) | Framework de tests unitarios |
| Pruebas | JaCoCo | 0.8.11 | ![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.11-FF6F00?style=flat-square) | Cobertura de código |
| Pruebas | MockMvc | — | ![MockMvc](https://img.shields.io/badge/MockMvc-6.x-lightgrey?style=flat-square) | Pruebas de controladores |
| Documentación | SpringDoc OpenAPI | 2.8.5 | ![Swagger](https://img.shields.io/badge/OpenAPI-2.8.5-85EA2D?style=flat-square&logo=swagger&logoColor=black) | Generación de documentación Swagger |
| Infraestructura | Docker | — | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white) | Contenerización de la aplicación |
| Infraestructura | Docker Compose | 3.9 | ![Compose](https://img.shields.io/badge/Compose-3.9-2496ED?style=flat-square&logo=docker&logoColor=white) | Orquestación de contenedores |
| Utilidades | Lombok | 1.18.30 | ![Lombok](https://img.shields.io/badge/Lombok-1.18.30-CC0000?style=flat-square) | Reducción de código boilerplate |
| Utilidades | Bean Validation | 3.x | ![Validation](https://img.shields.io/badge/Validation-3.x-lightgrey?style=flat-square) | Validación de datos en DTOs y entidades |
| IDE Recomendado | IntelliJ IDEA | — | ![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=flat-square&logo=intellij-idea&logoColor=white) | Entorno de desarrollo recomendado |

</div>

## 🔐 Modelo de Seguridad (RBAC)

El sistema implementa un modelo de seguridad **RBAC (Role-Based Access Control)** con permisos granulares, gestionado mediante **Spring Security** y autenticación basada en **JWT**.

---

### 🧩 Diagrama de Roles y Permisos

![Diagrama de Seguridad](docs/diagramaSecurityRoles.png)

---

### 📊 Matriz de Acceso (Permisos por Rol)

<div align="center">

| Rol       | READ | CREATE | UPDATE | DELETE |
|------------|------|--------|--------|--------|
| ADMIN      | ✅   | ✅     | ✅     | ✅     |
| USER       | ✅   | ✅     | ❌     | ❌     |
| DEVELOPER  | ✅   | ✅     | ✅     | ❌     |
| INVITED    | ✅   | ❌     | ❌     | ❌     |

</div>


## 🔄 Flujo de Autenticación y Autorización

![Flujo de Seguridad](docs/flujo_security.png)

---

### 📝 Descripción del Flujo

1️⃣ El cliente envía sus credenciales al endpoint `/auth/log-in`.

2️⃣ `UserDetailsServiceImpl` valida al usuario contra la base de datos.

3️⃣ Si las credenciales son válidas, `JwtUtils` genera un **token JWT** firmado con el algoritmo **HMAC256**.

4️⃣ El token se envía al cliente como parte de la respuesta.

5️⃣ En peticiones posteriores, el cliente incluye el token en el header:



6️⃣ El filtro `JWTokenValidator` intercepta la petición, valida el token y extrae las autoridades (roles y permisos).

7️⃣ **Spring Security** autoriza o deniega el acceso al endpoint según:
   - Las autoridades extraídas del token
   - La configuración definida en `SecurityConfig`

---

## 🎫 Estructura del Token JWT

El token generado contiene información clave para autenticación y autorización.  
Ejemplo decodificado:

```json
{
  "iss": "AUTHOJWT-BACKEND",
  "sub": "admin",
  "authorities": "ROLE_ADMIN,READ,CREATE,UPDATE,DELETE",
  "iat": 1640995200,
  "exp": 1640997000,
  "jti": "550e8400-e29b-41d4-a716-446655440000"
}
```
🔎 Significado de los Claims

iss → Emisor del token

sub → Usuario autenticado

authorities → Roles y permisos asignados

iat → Fecha de emisión

exp → Fecha de expiración

jti → Identificador único del token

## 📍 API Endpoints

A continuación se detallan todos los endpoints disponibles en la API.  
También puedes explorarlos de forma interactiva en **Swagger UI** una vez la aplicación esté corriendo.

---

## 🔑 Autenticación (`/auth`)

<div align="center">

| Método | Endpoint   | Descripción              | Request Body              | Response Body        | Código HTTP |
|--------|------------|--------------------------|---------------------------|----------------------|------------|
| POST   | /sign-up   | Registrar nuevo usuario  | `AuthCreateUserDTO`       | `AuthResponseDTO`    | 201 CREATED |
| POST   | /log-in    | Iniciar sesión           | `AuthLoginRequestDTO`     | `AuthResponseDTO`    | 200 OK      |

</div>

---

<details>
<summary><b>📝 Ejemplos de Request/Response para Autenticación</b></summary>

<br>

### 🆕 Registro — `POST /auth/sign-up`

```json
// Request
{
  "username": "nuevo_usuario",
  "password": "password123",
  "roleRequest": {
    "roleListName": ["USER", "INVITED"]
  }
}

// Response (201 CREATED)
{
  "username": "nuevo_usuario",
  "message": "User created successfully",
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": true
}
```
### 🔐 Login — `POST /auth/log-in`

```
// Request
{
  "username": "admin",
  "password": "admin123"
}

// Response (200 OK)
{
  "username": "admin",
  "message": "User logged successfully",
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": true
}
```
</details>


## 📂 Categorías (`/api/categorias`)

<div align="center">

| Método | Endpoint   | Descripción                              | Permiso Requerido | Código HTTP      |
|--------|------------|------------------------------------------|-------------------|------------------|
| GET    | /          | Listar todas las categorías activas     | READ              | 200 OK           |
| GET    | /{id}      | Obtener una categoría por ID            | READ              | 200 OK           |
| POST   | /create    | Crear una nueva categoría               | CREATE            | 201 CREATED      |
| PUT    | /{id}      | Actualizar una categoría                | UPDATE            | 200 OK           |
| DELETE | /{id}      | Eliminar (desactivar) una categoría     | DELETE            | 204 NO CONTENT   |

</div>

---

<details>
<summary><b>📝 Ejemplos de Request/Response para Categorías</b></summary>

<br>

### 📋 Listar Categorías — `GET /api/categorias`

```json
// Response (200 OK)
[
  {
    "id": 1,
    "nombre": "Electrónica",
    "descripcion": "Dispositivos electrónicos y gadgets",
    "activa": true,
    "fechaCreacion": "2024-01-15T10:30:00"
  }
]
```
### ➕ Crear Categoría — `POST /api/categorias/create`
```
// Request
{
  "nombre": "Hogar",
  "descripcion": "Productos para el hogar"
}

// Response (201 CREATED)
{
  "id": 2,
  "nombre": "Hogar",
  "descripcion": "Productos para el hogar",
  "activa": true,
  "fechaCreacion": "2024-01-15T10:32:00"
}
```
</details>

## 📦 Productos (`/api/productos`)

<div align="center">

| Método | Endpoint                    | Descripción                                   | Permiso Requerido | Código HTTP     |
|--------|----------------------------|-----------------------------------------------|-------------------|-----------------|
| GET    | /                          | Listar todos los productos activos            | READ              | 200 OK          |
| GET    | /{id}                      | Obtener un producto por ID                   | READ              | 200 OK          |
| GET    | /categoria/{categoriaId}   | Listar productos activos por categoría        | READ              | 200 OK          |
| POST   | /                          | Crear un nuevo producto                      | CREATE            | 201 CREATED     |
| PUT    | /{id}                      | Actualizar un producto existente              | UPDATE            | 200 OK          |
| DELETE | /{id}                      | Eliminar (desactivar) un producto             | DELETE            | 204 NO CONTENT  |

</div>

---

<details>
<summary><b>📝 Ejemplos de Request/Response para Productos</b></summary>

<br>

### ➕ Crear Producto — `POST /api/productos`

```json
// Request
{
  "name": "Laptop Gamer",
  "descripcion": "Laptop con RTX 4060, 16GB RAM",
  "precio": 1299.99,
  "categoriaId": 1
}
```

```
// Response (201 CREATED)
{
  "id": 1,
  "name": "Laptop Gamer",
  "descripcion": "Laptop con RTX 4060, 16GB RAM",
  "precio": 1299.99,
  "activo": true,
  "categoriaId": 1,
  "categoriaNombre": "Electrónica",
  "fechaCreacion": "2024-01-15T10:35:00"
}
```
### 📋 Listar Productos por Categoría — `GET /api/productos/categoria/1`
```
// Response (200 OK)
[
  {
    "id": 1,
    "name": "Laptop Gamer",
    "descripcion": "Laptop con RTX 4060, 16GB RAM",
    "precio": 1299.99,
    "activo": true,
    "categoriaId": 1,
    "categoriaNombre": "Electrónica",
    "fechaCreacion": "2024-01-15T10:35:00"
  }
]
```
</details>

📂 Estructura de Archivos
La organización del proyecto es clara y escalable, siguiendo una estructura modular por funcionalidad.

```
📦 crudstore-backend/
├── 📁 .mvn/                                   # Wrapper de Maven
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/mglopez/crudstore/
│   │   │   ├── 📁 config/                     # Configuraciones globales
│   │   │   │   ├── 📄 CorsConfig.java         # Configuración CORS
│   │   │   │   ├── 📄 DataInitializer.java    # Datos iniciales automáticos
│   │   │   │   └── 📄 SwaggerConfig.java      # Configuración de OpenAPI/Swagger
│   │   │   ├── 📁 modules/                    # Módulos funcionales
│   │   │   │   ├── 📁 auth/                    # 🔐 Módulo de autenticación
│   │   │   │   │   ├── 📁 api/                  # Capa de presentación (Controllers, DTOs)
│   │   │   │   │   ├── 📁 application/          # Capa de aplicación (Servicios)
│   │   │   │   │   ├── 📁 domain/               # Capa de dominio (Entidades, Enums)
│   │   │   │   │   └── 📁 infrastructure/       # Capa de infraestructura (Repos, Seguridad)
│   │   │   │   ├── 📁 categoria/                # 📂 Módulo de categorías
│   │   │   │   │   ├── 📁 api/
│   │   │   │   │   ├── 📁 application/
│   │   │   │   │   ├── 📁 domain/
│   │   │   │   │   └── 📁 infrastructure/
│   │   │   │   └── 📁 producto/                 # 📦 Módulo de productos
│   │   │   │       ├── 📁 api/
│   │   │   │       ├── 📁 application/
│   │   │   │       ├── 📁 domain/
│   │   │   │       └── 📁 infrastructure/
│   │   │   └── 📁 shared/                      # Código compartido
│   │   │       └── 📁 exception/                # Manejo global de errores
│   │   │           ├── 📄 GlobalExceptionHandler.java
│   │   │           ├── 📄 ErrorResponse.java
│   │   │           └── 📄 BusinessException.java (y subclases)
│   │   └── 📁 resources/
│   │       └── 📄 application.properties        # Configuración de la aplicación
│   └── 📁 test/
│       └── 📁 java/com/mglopez/crudstore/
│           └── 📁 modules/
│               ├── 📁 auth/                      # Tests del módulo auth
│               ├── 📁 categoria/
│               │   ├── 📁 api/
│               │   │   └── 📄 CategoriaControllerTest.java
│               │   └── 📁 application/
│               │       └── 📄 CategoriaServiceImplTest.java
│               └── 📁 producto/
│                   ├── 📁 api/
│                   │   └── 📄 ProductoControllerTest.java
│                   └── 📁 application/
│                       └── 📄 ProductoServiceImplTest.java
├── 📄 .gitignore
├── 📄 docker-compose.yml                         # Orquestación de contenedores
├── 📄 Dockerfile                                  # Imagen Docker de la aplicación
├── 📄 mvnw                                        # Wrapper de Maven (Unix)
├── 📄 mvnw.cmd                                    # Wrapper de Maven (Windows)
└── 📄 pom.xml                                     # Configuración de Maven
```


## 🗄️ Base de Datos

El modelo de datos está compuesto por **7 tablas principales** que reflejan la lógica de negocio y el sistema de seguridad RBAC.

<div align="center">
  <img src="docs/DiagramaEntidadRelacion.png" alt="Diagrama Entidad Relación" width="800"/>
</div>

---

### 📘 Descripción de las Tablas

#### 👤 `users`
Almacena la información de los usuarios del sistema:
- `username`
- `password`
- Flags de cuenta (`enabled`, `accountNonExpired`, etc.)

---

#### 🛡️ `roles`
Define los roles disponibles en el sistema:
- `ADMIN`
- `USER`
- `DEVELOPER`
- `INVITED`

---

#### 🔐 `permissions`
Define los permisos granulares del sistema:
- `READ`
- `CREATE`
- `UPDATE`
- `DELETE`

---

#### 🔄 `user_roles`
Tabla intermedia para la relación **muchos-a-muchos** entre:
- `users`
- `roles`

Un usuario puede tener múltiples roles.

---

#### 🔄 `role_permissions`
Tabla intermedia para la relación **muchos-a-muchos** entre:
- `roles`
- `permissions`

Cada rol puede tener múltiples permisos asociados.

---

#### 📂 `categorias`
Almacena las categorías de productos:
- `nombre`
- `descripcion`
- `activa`
- `fechaCreacion`

---

#### 📦 `productos`
Almacena los productos del sistema:
- `name`
- `descripcion`
- `precio`
- `activo`
- `fechaCreacion`
- `categoria_id` (clave foránea hacia `categorias`)

---

### 🧠 Diseño del Modelo

- Se implementa un esquema **normalizado**
- Se respeta el principio de **separación de responsabilidades**
- Seguridad desacoplada del dominio de negocio
- Relaciones many-to-many gestionadas mediante tablas intermedias
- Integridad referencial mediante claves foráneas


## ⚙️ Instalación y Ejecución

Sigue estos pasos para levantar el proyecto en tu entorno local.

---

### 📋 Prerrequisitos

- ☕ **JDK 17 o superior** ([Descargar](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))  
- 🐘 **Maven 3.8+** (o usa el wrapper `./mvnw` incluido)  
- 🐘 **PostgreSQL 15+** ([Descargar](https://www.postgresql.org/download/)) o **Docker**  
- 📬 **Postman** o **Insomnia** para probar la API ([Descargar Postman](https://www.postman.com/downloads/))  
- 🔧 *(Opcional)* IDE: IntelliJ IDEA, Eclipse, VS Code

---

### 🚀 Pasos de Instalación (Sin Docker)

#### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/MiguelG0929/crudstore-backend.git
cd crudstore-backend
```
2️⃣ Configurar la base de datos PostgreSQL

Accede a la consola de PostgreSQL y crea la base de datos:
```
sudo -u postgres psql
CREATE DATABASE crudstore_db;
\q
```
3️⃣ Configurar application.properties
Edita el archivo src/main/resources/application.properties con las credenciales de tu base de datos:

```
# DATA BASE CONFIGURATION
spring.datasource.url=jdbc:postgresql://localhost:5432/crudstore_db
spring.datasource.username=postgres
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/HIBERNATE CONFIGURATION
spring.jpa.hibernate.ddl-auto=create-drop  # O 'update' para desarrollo continuo
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# SERVER CONFIGURATION
server.port=9525

# JWT SECURITY (Puedes cambiar la clave si quieres)
security.jwt.key.private=13e84f751d69db68ab9a6a4e46b6f1c7ea3373482549e791b991d09de2d911a8
security.jwt.user.generator=AUTHOJWT-BACKEND
```
4️⃣ Compilar y ejecutar la aplicación
```
# Limpiar y compilar el proyecto
./mvnw clean install

# Ejecutar la aplicación
./mvnw spring-boot:run
```
5️⃣ Verificar la instalación

La aplicación debería estar corriendo en http://localhost:9525. Puedes verificarlo con:
!Nota es probable que por la seguridad deniegue el acceso desde esta URL
curl http://localhost:9525/actuator/health

O simplemente abrir en tu navegador e iniciar sesion con los usuarios predeterminados:
```
user: admin
password: admin123

user: user
password: user123

o crear usuario desde swagger
```
http://localhost:9525/swagger-ui.html
