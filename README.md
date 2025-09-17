# Challenge MercadoLibre - Backend API

## Resumen Ejecutivo

Implementación de una **API REST completa** que simula la funcionalidad principal de MercadoLibre, desarrollada como parte del desafío técnico. La solución incluye detalle de productos, búsqueda avanzada, recomendaciones, análisis de vendedores y comparación de productos, construida con **Spring Boot** y siguiendo las mejores prácticas de **Arquitectura Hexagonal**.

### Objetivos Cumplidos

- **API Backend Completa** - Todos los endpoints necesarios para soportar una página de detalle de producto
- **Arquitectura Limpia** - Implementación de Hexagonal Architecture con separación clara de responsabilidades
- **Manejo de Excepciones Robusto** - Sistema centralizado y consistente de manejo de errores
- **Testing Integral** - Cobertura completa de tests unitarios para controladores
- **Documentación Completa** - Endpoints documentados con ejemplos de uso
- **Datos de Prueba** - Base de datos H2 pre-cargada con productos realistas

## Arquitectura del Sistema

### Arquitectura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
├─────────────────────┬─────────────────────┬─────────────────┤
│   Driver Adapters   │                     │  Driven Adapters │
│                     │                     │                  │
│ ┌─────────────────┐ │                     │ ┌──────────────┐ │
│ │  REST API       │ │                     │ │  H2 Database │ │
│ │  Controllers    │ │                     │ │  Repository  │ │
│ │                 │ │                     │ │              │ │
│ │ • ItemDetail    │ │                     │ │ • JPA        │ │
│ │ • Search        │ │                     │ │ • SQL        │ │
│ │ • Trending      │ │                     │ │ • Data.sql   │ │
│ │ • Recommendations│ │                    │ │              │ │
│ │ • Seller        │ │                     │ └──────────────┘ │
│ │ • Comparison    │ │                     │                  │
│ └─────────────────┘ │                     │                  │
└─────────────────────┼─────────────────────┼─────────────────┘
                      │                     │
┌─────────────────────┼─────────────────────┼─────────────────┐
│                     │   APPLICATION LAYER │                  │
│                     │                     │                  │
│                     │ ┌─────────────────┐ │                  │
│          ┌──────────┤ │  Use Cases      │ ├─────────┐        │
│          │          │ │                 │ │         │        │
│          │          │ │ • GetItemDetail │ │         │        │
│          │          │ │ • SearchItems   │ │         │        │
│          │          │ │ • GetTrending   │ │         │        │
│          │          │ │ • Recommendations│ │        │        │
│          │          │ │ • SellerAnalytics│ │        │        │
│          │          │ │ • CompareItems  │ │         │        │
│          │          │ └─────────────────┘ │         │        │
│          │          │                     │         │        │
└──────────┼──────────┼─────────────────────┼─────────┼────────┘
           │          │                     │         │
┌──────────┼──────────┼─────────────────────┼─────────┼────────┐
│          │          │    DOMAIN LAYER     │         │        │
│          │          │                     │         │        │
│   ┌──────▼──────┐   │ ┌─────────────────┐ │   ┌─────▼─────┐  │
│   │   Input     │   │ │  Domain Models  │ │   │  Output   │  │
│   │   Ports     │   │ │                 │ │   │  Ports    │  │
│   │             │   │ │ • Item          │ │   │           │  │
│   │ (Interfaces)│   │ │ • Seller        │ │   │(Interfaces)│ │
│   │             │   │ │ • Category      │ │   │           │  │
│   │             │   │ │ • Search        │ │   │           │  │
│   │             │   │ │                 │ │   │           │  │
│   │             │   │ │ Exception       │ │   │           │  │
│   │             │   │ │ Handling        │ │   │           │  │
│   └─────────────┘   │ └─────────────────┘ │   └───────────┘  │
│                     │                     │                  │
└─────────────────────┴─────────────────────┴─────────────────┘
```

### Stack Tecnológico

- **Framework**: Spring Boot 3.5.5
- **Lenguaje**: Java 17
- **Base de Datos**: H2 Database (En memoria)
- **Build Tool**: Gradle 8.13
- **Testing**: JUnit 5 + Mockito + Spring Boot Test
- **Documentación**: OpenAPI/Swagger (Implícito)

## Modelo de Datos

### Entidades Principales
- **Items** - Productos principales con información básica
- **Categories** - Categorización jerárquica
- **Sellers** - Vendedores con reputación y métricas
- **Item_Attributes** - Atributos técnicos flexibles
- **Item_Pictures** - Imágenes con metadatos
- **Shipping_Methods** - Métodos de envío disponibles
- **Payment_Methods** - Opciones de pago y financiación
- **Item_Warranty** - Información de garantías

## Setup e Instalación

### Prerequisites
- Java 17+
- Gradle 7.0+

### Clonar y Ejecutar
```bash
git clone <repository-url>
cd ChallengeMeli
./gradlew bootRun
```

### Base de Datos
- H2 Database se inicializa automáticamente
- Datos de prueba se cargan desde `data.sql`
- Console H2: http://localhost:8081/h2-console

### Endpoints Base
- API Base URL: `http://localhost:8081/api/v1`
- Health Check: `http://localhost:8081/actuator/health`

## API Endpoints

### 1. Item Detail
**Obtener información completa del artículo**
```bash
# Obtener item por ID
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789" \
  -H "Accept: application/json"
```

### 2. Search API
**Búsqueda avanzada de items**
```bash
# Búsqueda básica
curl -X GET "http://localhost:8081/api/v1/items/search?query=iPhone&limit=5" \
  -H "Accept: application/json"

# Búsqueda con filtros
curl -X GET "http://localhost:8081/api/v1/items/search?query=Samsung&limit=3&categoryId=MLA1055" \
  -H "Accept: application/json"

# Búsqueda por rango de precios
curl -X GET "http://localhost:8081/api/v1/items/search?minPrice=50000&maxPrice=200000&limit=10" \
  -H "Accept: application/json"
```

### 3. Trending Items
**Items populares y tendencias**
```bash
# Best sellers
curl -X GET "http://localhost:8081/api/v1/trending/best-sellers?limit=5" \
  -H "Accept: application/json"

# Most viewed
curl -X GET "http://localhost:8081/api/v1/trending/most-viewed?limit=5" \
  -H "Accept: application/json"

# Trending por categoría
curl -X GET "http://localhost:8081/api/v1/categories/MLA1055/trending?limit=3" \
  -H "Accept: application/json"
```

### 4. Recommendations
**Sistema de recomendaciones**
```bash
# Recomendaciones para un item
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789/recommendations?limit=5" \
  -H "Accept: application/json"

# Items similares
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789/similar?limit=5" \
  -H "Accept: application/json"

# Frecuentemente comprados juntos
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789/frequently-bought-together?limit=3" \
  -H "Accept: application/json"

# También vistos
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789/also-viewed?limit=5" \
  -H "Accept: application/json"
```

### 5. Seller Analytics
**Información de vendedores**
```bash
# Perfil del vendedor
curl -X GET "http://localhost:8081/api/v1/sellers/12345/profile" \
  -H "Accept: application/json"

# Items del vendedor
curl -X GET "http://localhost:8081/api/v1/sellers/12345/items?limit=5&offset=0" \
  -H "Accept: application/json"

# Detalles de reputación
curl -X GET "http://localhost:8081/api/v1/sellers/12345/reputation-details" \
  -H "Accept: application/json"

# Vendedores mejor valorados
curl -X GET "http://localhost:8081/api/v1/sellers/top-rated?limit=5" \
  -H "Accept: application/json"
```

### 6. Product Comparison
**Comparador de productos**
```bash
# Comparar items
curl -X GET "http://localhost:8081/api/v1/items/compare?ids=MLA123456789,MLA234567890" \
  -H "Accept: application/json"
```

### 7. Legacy Endpoints
**Endpoints de medios de pago (legado)**
```bash
# Medios de pago
curl -X GET "http://localhost:8081/api/v1/medios-pago" \
  -H "Accept: application/json"

# Medios de pago activos
curl -X GET "http://localhost:8081/api/v1/medios-pago/active" \
  -H "Accept: application/json"
```

## Respuestas de API

### Ejemplo Response - Item Detail
```json
{
  "success": true,
  "message": "Item detail retrieved successfully",
  "data": {
    "id": "MLA123456789",
    "title": "iPhone 15 Pro 128GB Titanio Natural",
    "price": {
      "amount": 1299999,
      "currency": "ARS",
      "decimals": 2,
      "formatted": "$1.299.999,00"
    },
    "condition": "new",
    "availableQuantity": 25,
    "soldQuantity": 150,
    "category": {
      "id": "MLA1055",
      "name": "Celulares y Teléfonos",
      "pathFromRoot": "Electrónicos, Audio y Video > Celulares y Teléfonos"
    },
    "seller": {
      "id": 12345,
      "nickname": "TECHSTORE_OFICIAL",
      "reputation": {
        "level": "5_green",
        "powerSeller": "platinum",
        "transactions": 5420,
        "positiveRating": 0.98
      }
    },
    "pictures": [...],
    "attributes": [...],
    "shipping": [...],
    "warranty": {...}
  }
}
```

## Testing

### Script de Pruebas Rápidas
```bash
#!/bin/bash
echo "Testing endpoints principales..."
curl -s "http://localhost:8081/api/v1/items/MLA123456789" | jq .success
curl -s "http://localhost:8081/api/v1/items/search?query=iPhone&limit=3" | jq .success

echo "Testing trending..."
curl -s "http://localhost:8081/api/v1/trending/best-sellers?limit=3" | jq .success
curl -s "http://localhost:8081/api/v1/trending/most-viewed?limit=3" | jq .success

echo "Testing recomendaciones..."
curl -s "http://localhost:8081/api/v1/items/MLA123456789/recommendations?limit=3" | jq .success
curl -s "http://localhost:8081/api/v1/items/MLA123456789/similar?limit=3" | jq .success

echo "Testing vendedores..."
curl -s "http://localhost:8081/api/v1/sellers/12345/profile" | jq .success
curl -s "http://localhost:8081/api/v1/sellers/12345/items?limit=3" | jq .success

echo "Testing completado"
```

## Sistema de Manejo de Excepciones

### **Característica Destacada: Control de Excepciones Centralizado**

Una de las implementaciones más robustas del proyecto es el **sistema centralizado de manejo de excepciones** que garantiza respuestas consistentes y manejo adecuado de errores en toda la API.

#### Arquitectura de Excepciones

```
domain/common/exception/
├── BaseException.java              // Excepción base con códigos de error
├── BusinessException.java          // Errores de lógica de negocio
├── ValidationException.java        // Errores de validación de datos
├── BaseNotFoundException.java      // Errores de recursos no encontrados
└── codes/ExceptionCode.java        // Catálogo centralizado de códigos
```

#### Handler Global de Excepciones

El `GlobalExceptionHandler` intercepta **todas las excepciones** y las transforma en respuestas HTTP apropiadas:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex) {
        return json(HttpStatus.BAD_REQUEST,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return json(HttpStatus.CONFLICT,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BaseNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(BaseNotFoundException ex) {
        return json(HttpStatus.NOT_FOUND,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR,
                   ApiResponse.error("INTERNAL_SERVER_ERROR", "Error interno del servidor"));
    }
}
```

#### Sistema de Códigos de Error

Códigos estandarizados para diferentes tipos de errores:

```java
public class ExceptionCode {
    // Validación (ERR-001 a ERR-007)
    public static final String NULL_FIELD = "ERR-001";
    public static final String INVALID_VALUE = "ERR-003";
    public static final String INVALID_RANGE = "ERR-006";

    // Recursos (ERR-201 a ERR-204)
    public static final String RESOURCE_NOT_FOUND = "ERR-201";
    public static final String RESOURCE_CONFLICT = "ERR-203";

    // Items específicos (ERR-601 a ERR-604)
    public static final String ITEM_NOT_FOUND = "ERR-601";
    public static final String ITEM_NOT_ACTIVE = "ERR-602";

    // Sistema (ERR-901 a ERR-905)
    public static final String INTERNAL_SERVER_ERROR = "ERR-901";
}
```

#### Formato de Respuesta Estándar

Todas las respuestas siguen un formato consistente:

```json
{
  "success": true|false,
  "message": "Descripción del resultado",
  "description": "Detalles adicionales (solo en errores)",
  "errorCode": "ERR-XXX",
  "data": { /* Datos de respuesta */ },
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Beneficios del Sistema

1. **Consistencia**: Todas las respuestas tienen el mismo formato
2. **Trazabilidad**: Códigos únicos para cada tipo de error
3. **Debugging**: Mensajes técnicos y amigables separados
4. **Mantenibilidad**: Centralización facilita cambios y mejoras
5. **Escalabilidad**: Fácil agregar nuevos tipos de excepciones

## Testing Strategy

### Cobertura de Tests

- **Controller Tests**: 4 archivos de test completos
- **Exception Handler Tests**: Tests para manejo de errores
- **Use Case Tests**: Tests de lógica de negocio
- **Integration Tests**: Tests end-to-end

### Tests de Controladores

#### ItemDetailControllerTest
- Obtención exitosa de detalles de producto
- Manejo de productos no encontrados
- Manejo de productos inactivos
- Validación de parámetros inválidos

#### TrendingControllerTest
- Best sellers con límite por defecto y personalizado
- Most viewed con parámetros válidos
- Trending por categoría
- Manejo de categorías inexistentes

#### RecommendationControllerTest
- Recomendaciones de productos similares
- Frecuentemente comprados juntos
- También vistos
- Manejo de productos inexistentes

#### SellerControllerTest
- Perfil del vendedor
- Items del vendedor con paginación
- Detalles de reputación
- Top vendedores
- Validación de vendedores inexistentes

#### ItemSearchControllerTest
- Búsqueda básica y avanzada
- Filtros múltiples (precio, categoría, envío)
- Comparación de productos
- Manejo de consultas sin resultados
- Validación de parámetros inválidos

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos de controladores
./gradlew :rest-driver:test

# Tests con reporte de cobertura
./gradlew test jacocoTestReport
```

## Base de Datos y Datos de Prueba

### Estructura de Datos

La aplicación utiliza **H2 Database** en memoria con datos precargados:

- **10 productos** de diferentes categorías (tecnología, hogar, moda, deportes)
- **5 vendedores** con diferentes niveles de reputación
- **7 categorías** organizadas jerárquicamente
- Atributos, imágenes, métodos de envío y pago para cada producto

### Productos de Ejemplo:

| ID | Producto | Precio (ARS) | Categoría |
|---|---|---|---|
| MLA123456789 | iPhone 15 Pro 128GB | $12,999.99 | Celulares |
| MLA234567890 | Samsung Galaxy S24 Ultra | $8,999.99 | Celulares |
| MLA345678901 | MacBook Air 13" M2 | $15,999.99 | Computación |
| MLA456789012 | Sony WH-1000XM5 | $2,999.99 | Audio |
| MLA567890123 | Smart TV Samsung 55" 4K | $7,999.99 | TV |

### Vendedores de Ejemplo:

| ID | Vendedor | Reputación | Nivel |
|---|---|---|---|
| 12345 | TECHSTORE_OFICIAL | 5_green | Platinum |
| 67890 | SAMSUNG_OFICIAL | 5_green | Platinum |
| 11111 | LEVI_STORE_AR | 4_light_green | Gold |
| 22222 | DEPORTES_EXTREMOS | 3_yellow | Regular |

## Decisiones Arquitectónicas Clave

### 1. **Arquitectura Hexagonal**
- **Justificación**: Separación clara entre lógica de negocio e infraestructura
- **Beneficio**: Facilita testing, mantenimiento y cambios de tecnología

### 2. **H2 Database en Memoria**
- **Justificación**: Cumple requisito de no usar bases de datos reales
- **Beneficio**: Setup instantáneo, datos consistentes, ideal para desarrollo

### 3. **Sistema de Excepciones Centralizado**
- **Justificación**: Garantiza consistencia en respuestas de error
- **Beneficio**: Mejor debugging, mantenimiento y experiencia de usuario

### 4. **DTOs Específicos por Endpoint**
- **Justificación**: Optimiza la transferencia de datos según necesidades
- **Beneficio**: Mejor rendimiento y flexibilidad en evolución de API

### 5. **Testing Integral**
- **Justificación**: Garantiza calidad y facilita refactoring
- **Beneficio**: Confianza en despliegues y mantenimiento

## Quick Start

### Prerrequisitos

- Java 17 o superior
- Gradle 7.0+ (o usar wrapper incluido)

### Ejecutar la Aplicación

```bash
# Clonar el repositorio
git clone <repository-url>
cd ChallengeMeli

# Ejecutar con Gradle Wrapper (recomendado)
./gradlew bootRun

# La aplicación estará disponible en:
# http://localhost:8081/api/v1
```

### Verificar la Instalación

```bash
# Health Check
curl http://localhost:8081/actuator/health

# Test básico - Obtener detalle de producto
curl "http://localhost:8081/api/v1/items/MLA123456789"

# Búsqueda
curl "http://localhost:8081/api/v1/items/search?query=iPhone&limit=5"
```

### Consola H2 Database

Acceder a la base de datos en: http://localhost:8081/h2-console

- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

## Extensibilidad y Mejoras Futuras

### Características que se pueden Agregar:

1. **Caching con Redis** - Para mejorar performance
2. **Rate Limiting** - Para controlar uso de API
3. **API Versioning** - Para evolución controlada
4. **Métricas y Monitoring** - Con Micrometer/Prometheus
5. **Documentación OpenAPI** - Swagger UI automático
6. **Seguridad JWT** - Autenticación y autorización
7. **Elasticsearch** - Para búsquedas más potentes
8. **Message Queues** - Para procesos asíncronos

### Evolución de Arquitectura:

```
Actual: Monolito → Microservicios
├── Item Service
├── Search Service
├── Recommendation Service
├── Seller Service
└── Notification Service
```

## Uso de Herramientas GenAI

Durante el desarrollo se utilizaron herramientas de **Inteligencia Artificial Generativa** para:

1. **Generación de Datos de Prueba** - Productos realistas y variados
2. **Optimización de Consultas** - Mejora de queries de base de datos
3. **Documentación de API** - Ejemplos y descripciones detalladas
4. **Testing Strategy** - Casos de prueba comprehensivos
5. **Code Review** - Detección de posibles mejoras

---

### **Este proyecto demuestra:**

- **Arquitectura Clean** - Separación de responsabilidades
- **Manejo de Errores** - Sistema centralizado y consistente
- **API Design** - Endpoints RESTful bien diseñados
- **Documentación** - Completa y detallada
- **Escalabilidad** - Preparado para crecimiento
