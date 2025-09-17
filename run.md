# Cómo Ejecutar el Proyecto

## Prerrequisitos

- **Java 17** o superior
- **Gradle 7.0** o superior (o usar el wrapper incluido)

## Verificar Instalación

```bash
# Verificar Java
java -version

# Verificar Gradle (opcional, se puede usar el wrapper)
gradle -version
```

## Ejecutar el Proyecto

### Opción 1: Usando Gradle Wrapper (Recomendado)

```bash
# En el directorio raíz del proyecto
./gradlew bootRun
```

En Windows:
```cmd
gradlew.bat bootRun
```

### Opción 2: Usando Gradle Instalado

```bash
gradle bootRun
```

## Verificar que la Aplicación está Funcionando

Una vez que la aplicación esté ejecutándose, verás en la consola algo como:

```
Started ChallengeMeliApplication in X.XXX seconds
```

La aplicación estará disponible en: **http://localhost:8081/api/v1**

### Endpoints de Verificación

```bash
# Health Check
curl -X GET "http://localhost:8081/actuator/health"

# Test básico - Obtener un item específico
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789" \
  -H "Accept: application/json"
```

## Configuración de Base de Datos

La aplicación usa **H2 Database** en memoria, que se configura automáticamente:

- **URL**: jdbc:h2:mem:testdb
- **Usuario**: sa
- **Contraseña**: (vacía)
- **Console H2**: http://localhost:8081/h2-console

## Endpoints Disponibles con Ejemplos

### 1. Item Detail API
```bash
# Obtener detalle completo de un item
curl -X GET "http://localhost:8081/api/v1/items/MLA123456789" \
  -H "Accept: application/json"
```

### 2. Search API
```bash
# Búsqueda básica
curl -X GET "http://localhost:8081/api/v1/items/search?query=iPhone&limit=5" \
  -H "Accept: application/json"

# Búsqueda con filtros avanzados
curl -X GET "http://localhost:8081/api/v1/items/search?query=Samsung&categoryId=MLA1055&limit=3" \
  -H "Accept: application/json"

# Búsqueda por rango de precios
curl -X GET "http://localhost:8081/api/v1/items/search?minPrice=50000&maxPrice=200000&limit=10" \
  -H "Accept: application/json"
```

### 3. Trending Items
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
```

### 5. Seller Analytics
```bash
# Perfil del vendedor
curl -X GET "http://localhost:8081/api/v1/sellers/12345/profile" \
  -H "Accept: application/json"

# Items del vendedor
curl -X GET "http://localhost:8081/api/v1/sellers/12345/items?limit=5&offset=0" \
  -H "Accept: application/json"

# Vendedores mejor valorados
curl -X GET "http://localhost:8081/api/v1/sellers/top-rated?limit=5" \
  -H "Accept: application/json"
```

### 6. Product Comparison
```bash
# Comparar items
curl -X GET "http://localhost:8081/api/v1/items/compare?ids=MLA123456789,MLA234567890" \
  -H "Accept: application/json"
```

## Script de Pruebas Completo

Crear un archivo `test-api.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8081/api/v1"

echo "=== Testing MercadoLibre Challenge API ==="
echo ""

echo "1. Testing Item Detail API..."
curl -s "$BASE_URL/items/MLA123456789" | jq .success
echo ""

echo "2. Testing Search API..."
curl -s "$BASE_URL/items/search?query=iPhone&limit=3" | jq .success
echo ""

echo "3. Testing Trending API..."
curl -s "$BASE_URL/trending/best-sellers?limit=3" | jq .success
echo ""

echo "4. Testing Recommendations..."
curl -s "$BASE_URL/items/MLA123456789/recommendations?limit=3" | jq .success
echo ""

echo "5. Testing Seller Analytics..."
curl -s "$BASE_URL/sellers/12345/profile" | jq .success
echo ""

echo "6. Testing Product Comparison..."
curl -s "$BASE_URL/items/compare?ids=MLA123456789,MLA234567890" | jq .success
echo ""

echo "=== API Testing Completado ==="
```

Ejecutar el script:
```bash
chmod +x test-api.sh
./test-api.sh
```

## Datos de Prueba

La aplicación viene pre-cargada con datos de prueba que incluyen:

- **10 productos** de diferentes categorías (tecnología, hogar, moda, deportes)
- **5 vendedores** con diferentes niveles de reputación
- **7 categorías** organizadas jerárquicamente
- Atributos, imágenes, métodos de envío y pago para cada producto

### Productos de Ejemplo:
- `MLA123456789` - iPhone 15 Pro 128GB Titanio Natural
- `MLA234567890` - Samsung Galaxy S24 Ultra 256GB
- `MLA345678901` - MacBook Air 13" M2 256GB
- `MLA456789012` - Sony WH-1000XM5 Auriculares
- `MLA567890123` - Smart TV Samsung 55" 4K UHD
- `MLA789012345` - Zapatillas Nike Air Max 270
- `MLA890123456` - Jean Levi's 501 Original Fit

### Vendedores de Ejemplo:
- `12345` - TECHSTORE_OFICIAL (Platinum seller)
- `67890` - SAMSUNG_OFICIAL (Platinum seller)
- `11111` - LEVI_STORE_AR (Gold seller)
- `22222` - DEPORTES_EXTREMOS (Regular seller)
- `33333` - HOGAR_PREMIUM (Gold Pro seller)

## Compilar y Construir

### Compilar el proyecto
```bash
./gradlew build
```

### Ejecutar tests
```bash
./gradlew test
```

### Limpiar build
```bash
./gradlew clean
```

### Build sin tests
```bash
./gradlew build -x test
```

## Solución de Problemas

### Error: Puerto 8081 en uso
```bash
# Cambiar puerto usando variable de entorno
SERVER_PORT=8082 ./gradlew bootRun

# O editar application.yml
```

### Error: Java version
Asegúrate de tener Java 17 o superior:
```bash
export JAVA_HOME=/path/to/java17
java -version
```

### Error: Permisos en gradlew (Linux/Mac)
```bash
chmod +x gradlew
```

### Verificar que el servicio está corriendo
```bash
# Verificar el puerto
lsof -i :8081

# O usar netstat
netstat -an | grep 8081
```

## Configuración Adicional

### Variables de Entorno Disponibles:
- `SERVER_PORT`: Puerto del servidor (default: 8081)
- `SPRING_PROFILES_ACTIVE`: Perfil activo (default: dev)

### Archivos de Configuración:
- `application.yml`: Configuración principal
- `schema.sql`: Esquema de base de datos
- `data.sql`: Datos de prueba

## Detener la Aplicación

Para detener la aplicación, presiona `Ctrl + C` en la terminal donde se está ejecutando.

---

**La aplicación estará corriendo en http://localhost:8081/api/v1**