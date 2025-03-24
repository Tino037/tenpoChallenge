# Tenpo Challenge

Este proyecto es una API REST que permite realizar cálculos con porcentajes y mantener un historial de las operaciones realizadas.

## Requisitos Previos

- Docker y Docker Compose
- Postman (opcional, para la segunda forma de ejecución)

## Formas de Ejecutar la Aplicación

### 1. Ejecución Local con Interfaz Web

Esta forma permite probar la aplicación a través de una interfaz web amigable.

1. Clonar el repositorio:
```bash
git clone [https://github.com/Tino037/tenpoChallenge.git]
cd tenpoChallenge
```

2. Dar permisos de ejecución al script de inicio:
```bash
chmod +x start.sh
```

3. Ejecutar el script de inicio:
```bash
./start.sh
```

4. Probar aplicación el navegador que se abrirá automáticamente


### 2. Ejecución con Docker y Pruebas en Postman

Esta forma permite probar la API directamente a través de Postman.

1. Clonar el repositorio:
```bash
git clone [https://github.com/Tino037/tenpoChallenge.git]
cd tenpoChallenge
```

2. Ejecutar con Docker Compose:
```bash
docker-compose up -d
```

3. Endpoints Disponibles:

#### Cálculo de Porcentaje
```
GET http://localhost:8080/tenpo/addPorcentage/{value1}/{value2}
```
- **Parámetros:**
  - `value1`: Primer valor (número)
  - `value2`: Segundo valor (número)
- **Ejemplo:** `http://localhost:8080/tenpo/addPorcentage/100/200`

#### Historial de Solicitudes
```
GET http://localhost:8080/info/history
```
- **Parámetros Query:**
  - `page`: Número de página (default: 0)
  - `size`: Tamaño de página (default: 10)
- **Ejemplo:** `http://localhost:8080/info/history?page=0&size=10`

#### Estado del Caché
```
GET http://localhost:8080/info/cache
```
- **Ejemplo:** `http://localhost:8080/info/cache`

## Detalles Técnicos

- La aplicación utiliza Spring Boot 3.2.3
- Base de datos PostgreSQL
- Caché implementado con Redis


## Notas Adicionales

- La aplicación incluye manejo de errores y validaciones
- Se implementa caché para optimizar el rendimiento
- Se mantiene un historial de todas las operaciones realizadas
- Se utilizó WebFlux a pedido del enunciado
    - Los endpoints retornan Mono de reactor.core
    - Se utilizo Mono.just() para la creación de flujos en las excepciones
    - Se utilizó ReactiveRedisTemplate para operaciones reactivas con Redis

En resumen se utilizó Webflux para aprovechar las ventajas de la programación reactiva como mejorar el rendimiento al no ser bloqueante,
mejor para el uso de redis, mejor para la escalabilidad.
Se utilizo JPA como ORM, entiendo que es bloqueante pero no hubo dispoinibilidad para utilizar r2dbc