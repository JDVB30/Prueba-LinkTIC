# Prueba-LinkTIC
Este repositorio está destinado para presentar la prueba técnica predispuesta por la empresa linktic para el cargo de desarrollador backend semi-senior

Este proyecto implementa una arquitectura de microservicios resiliente para la gestión de productos e inventarios, desarrollada con Java 17, Spring Boot 3 y orientada a contenedores con Docker.

# 1. Arquitectura y Decisiones Técnicas
Se ha diseñado un sistema desacoplado siguiendo el estándar JSON API y principios de Resiliencia.

Componentes:
Product Service (Puerto 8081): Microservicio encargado del CRUD de catálogo.

Inventory Service (Puerto 8082): Microservicio que gestiona el stock y valida la existencia de productos mediante comunicación inter-servicio.

Justificación de Decisiones:
Resilience4j (Circuit Breaker & Retry): Se implementó para evitar fallos en cascada. Si el servicio de productos no responde, el servicio de inventario activa un fallback controlado.

Comunicación Síncrona: Se utilizó RestTemplate configurado con timeouts estrictos y reintentos para garantizar la consistencia inmediata en la validación de productos.

Seguridad: Implementación de un filtro personalizado de API Key (X-API-KEY) para asegurar los endpoints sin la sobrecarga de un servidor de identidad externo.

Event Logging: Los cambios de inventario emiten logs estructurados en JSON para facilitar la observabilidad en entornos reales (ELK/Splunk).

# 2. Instrucciones de Ejecución (Docker)
Para garantizar que los servicios suban limpios y con todas las configuraciones de seguridad aplicadas, siga estos pasos:

 1. Clonar el repositorio
    git clone <tu-enlace-al-repo>
    cd <nombre-carpeta>

 2. Limpieza y Construcción (Recomendado para asegurar que Swagger cargue)
    docker-compose down -v
    docker-compose up --build -d

# 3. Documentación de Endpoints (Swagger)
Una vez levantados los servicios, puede acceder a la documentación interactiva y probar los endpoints:

Product Service: http://localhost:8081/swagger-ui.html

Inventory Service: http://localhost:8082/swagger-ui.html

Nota: Haga clic en el botón "Authorize" e ingrese el valor SECRET123 para habilitar las peticiones.

# 4. Ejecución de Pruebas
Pruebas Unitarias e Integración:
Desde la raíz de cada microservicio, ejecute:

Bash

mvn test
Cobertura: Se incluyen tests para creación/actualización de productos, manejo de errores (404 Not Found) y validación de seguridad.

Prueba de Resiliencia (Circuit Breaker):
Detenga el servicio de productos: docker stop product-service

Intente consultar stock en el servicio de inventario: GET http://localhost:8082/api/v1/inventory/1

Resultado: Recibirá una respuesta 503 controlada bajo el estándar JSON API.

# 5. Diagrama de Interacción
Client → Request (with API Key) → Inventory Service.

Inventory Service → API Key Validation (Filter).

Inventory Service → HTTP Call (with Retry/Timeout) → Product Service.

Product Service → Response → Inventory Service.

Inventory Service → Log Event & Response → Client.
