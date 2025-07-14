# 🧩 Microservices Project: Product & Inventory Service

Este proyecto demuestra cómo diseñar y conectar microservicios utilizando Spring Boot, con comunicación vía HTTP REST utilizando **OpenFeign**, persistencia con **PostgreSQL**, pruebas automatizadas con **JUnit**, y despliegue local a través de **Docker Compose**.

---

## 📦 Microservicios

### 💒 Product Service

- Gestiona el CRUD de productos.
- Puerto por defecto: `8081`.
- Expone endpoints para:
  - Crear productos
  - Consultar productos (por ID y paginación)
  - Actualizar productos
  - Eliminar productos

### 📦 Inventory Service

- Administra el stock de productos.
- Puerto por defecto: `8082`.
- Se comunica con Product Service para obtener datos del producto por ID.
- Expone endpoints para:
  - Consultar el stock de un producto
  - Actualizar el stock
  - Listar todos los inventarios

---

## 🚀 Instalación y Ejecución

### ⚙️ Requisitos Previos

- Docker
- Docker Compose
- Git
- Java 17


### 🔧 Clonar y Ejecutar

```bash
git clone https://github.com/Martinfbr/microservices-project.git
cd microservices-project
docker-compose up --build
```
Esto lanzará ambos microservicios y sus respectivas bases de datos PostgreSQL.


### 🔍 Endpoints de prueba

| Servicio  | URL Swagger                                                                    | Puerto |
| --------- | ------------------------------------------------------------------------------ | ------ |
| Product   | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | 8081   |
| Inventory | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) | 8082   |

---

Puedes usar Postman o Swagger para probar los endpoints.


## 🧪 Pruebas Automatizadas

Se incluyen pruebas unitarias y de integración para asegurar la calidad del código:

- ✅ Pruebas de `Service`, `Controller` y `Mapper`
- ✅ Manejo de errores probado (excepciones personalizadas)
- ✅ Alta cobertura con **JaCoCo** (> 90%)
- ✅ Uso de **Testcontainers** para integración con PostgreSQL

Generación de reporte Jacoco:

```bash
./gradlew clean test jacocoTestReport
```

Ruta del reporte HTML:\
`build/reports/jacoco/test/html/index.html`
## 🧐 Decisiones Técnicas

| Decisión                                    | Justificación                                                                 |
| ------------------------------------------- | ----------------------------------------------------------------------------- |
| `OpenFeign` para comunicación               | Facilita la llamada HTTP REST entre microservicios sin mucha configuración    |
| `Docker Compose`                            | Permite iniciar múltiples servicios y bases de datos fácilmente               |
| `PostgreSQL`                                | Base de datos robusta y común en producción                                   |
| `Testcontainers`                            | Pruebas de integración realistas usando PostgreSQL en contenedor              |
| `SpringDoc / Swagger`                       | Documentación automática de APIs REST                                         |
| Separación por capas                        | Controller → Service → Repository, manteniendo un código limpio y desacoplado |
| Validaciones con Bean Validation (`@Valid`) | Se asegura que las entradas a la API estén correctamente estructuradas        |

---



## 📁 Estructura del Proyecto

```
microservices-project/
├── docker-compose.yml
├── product-service/
│   ├── Dockerfile
│   ├── src/
│   └── README.md
├── inventory-service/
│   ├── Dockerfile
│   ├── src/
│   └── README.md
└── assets/
    └── DiagramaArquitectura.png
```

🧽 Diagrama de Arquitectura
![Arquitectura](https://github.com/Martinfbr/microservices-project/blob/main/assets/Diagrama-Arquitectura.png)

🧪 Pruebas Automatizadas → Resultados de Cobertura
![Inventory Service Coverage](https://github.com/Martinfbr/microservices-project/blob/main/assets/Pruebas-inventory-service.png)
![Product Service Coverage](https://github.com/Martinfbr/microservices-project/blob/main/assets/Pruebas-pruduct-service.png)


## 🚀 Propuesta de mejoras y escalabilidad futura

- ➕ **Autenticación y Autorización**: Agregar OAuth2 o JWT para proteger los endpoints.
- ➕ **Manejo de eventos con mensajería (RabbitMQ/Kafka)**: Publicar eventos cuando se actualiza el inventario o se crea un producto.
- ➕ **Circuit Breaker y Retry con Resilience4J**: Mejor tolerancia a fallos en las llamadas entre microservicios.
- ➕ **API Gateway con Spring Cloud Gateway**: Un único punto de entrada y gestión de rutas.
- ➕ **Monitorización con Prometheus + Grafana**: Para métricas, trazabilidad y alertas.
- ➕ **Configuración centralizada con Spring Cloud Config**: Manejo externo de propiedades por ambiente.
- ➕ **Escalabilidad horizontal con Kubernetes**: Preparar los servicios para correr en un clúster orquestado.