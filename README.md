# Meeting Room API

API REST para reservar salas de reuniones, construida con **Java 17**, **Spring Boot 3**, **JWT**, **H2** y **Flyway**.

## Requisitos

- JDK 17 o superior
- Maven 3.8+

## Cómo ejecutarlo

```bash
mvn spring-boot:run
```

La API queda en `http://localhost:8080`.

Consola H2 (opcional): `http://localhost:8080/h2-console`  
- JDBC URL: `jdbc:h2:file:./data/meetingroom`  
- User: `sa`  
- Password: *(vacío)*

### Credenciales demo

| Usuario | Password |
|---------|----------|
| `admin` | `admin123` |

## Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| `POST` | `/api/auth/login` | No | Obtiene JWT |
| `POST` | `/api/rooms` | JWT | Crea sala |
| `POST` | `/api/rooms/list` | No | Lista salas (paginado) |
| `POST` | `/api/reservations` | JWT | Crea reserva |
| `POST` | `/api/reservations/list` | No | Lista reservas (filtro sala/fecha + paginado) |
| `DELETE` | `/api/reservations/{id}` | JWT | Cancela reserva |

### Ejemplos curl

```bash
# Login
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Crear sala (reemplaza TOKEN)
curl -s -X POST http://localhost:8080/api/rooms \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"name":"Sala Azul","location":"Piso 3","capacity":10}'

# Listar salas (público)
curl -s -X POST http://localhost:8080/api/rooms/list \
  -H "Content-Type: application/json" \
  -H "page: 0" \
  -H "size: 20" \
  -d '{"active": true}'

# Crear reserva
curl -s -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"roomId":"ROOM_UUID","responsible":"Ana Pérez","startAt":"2026-12-01T10:00:00Z","endAt":"2026-12-01T11:00:00Z"}'

# Listar reservas por sala y fecha
curl -s -X POST http://localhost:8080/api/reservations/list \
  -H "Content-Type: application/json" \
  -H "page: 0" \
  -H "size: 20" \
  -d '{"roomId":"ROOM_UUID","date":"2026-12-01"}'

# Cancelar reserva
curl -s -X DELETE http://localhost:8080/api/reservations/RESERVATION_UUID \
  -H "Authorization: Bearer TOKEN"
```

### Códigos de error esperados

- **400**: validación (`name` vacío, `capacity <= 0`, `endAt <= startAt`, etc.) con `fieldErrors` cuando aplica.
- **401**: mutaciones sin JWT válido.
- **404**: sala/reserva inexistente.
- **409**: solape de horario en la misma sala (`ACTIVE`).

## Tests

```bash
mvn test
```

Incluye:

1. Unitario (`CreateReservationCommandHandler`): solape → `ConflictException` (409).
2. Unitario: `endAt <= startAt` → `BadRequestException` (400).
3. Integración MockMvc: crear reserva OK, solape 409, listado público 200.
4. Extra: validación de sala 400 con `fieldErrors`; cancelación exige JWT.

## Decisiones técnicas

- **Arquitectura CQRS** según `.cursor/rules/backend-spring.mdc`:
  - `application/commands|queries` → handlers que implementan `Command`/`Query`
  - `domain/entities` + `domain/interfaces` (sin JPA)
  - `infrastructure/controllers` delgados (`execute()`)
  - `infrastructure/persistence` con `*Model`, MapStruct y `*RepositoryImpl`
- **Sin `*Service` clásicos** en módulos de negocio.
- **JWT** (jjwt) con usuario demo en memoria: auth mínima sin tabla de usuarios.
- **H2 file + Flyway**: cero Docker para demos; `ddl-auto: validate` para que el esquema viva en migraciones.
- **Listados `POST /list`** con headers `page`/`size` y body con `filters`/`ordersBy`.
- **Cancelación soft** (`ACTIVE`/`CANCELLED`): conserva historial y libera el horario.
- **Conflicto**: solape `start < otherEnd AND end > otherStart` solo contra reservas `ACTIVE`.
- Mensajes de error al cliente en **español**; código en **inglés**.
- Auth queda fuera del CQRS de negocio (login/JWT utilitario).

## Con más tiempo

- Perfil `postgres` + Testcontainers.
- Usuarios y roles en BD (registro, refresh tokens).
- Lock optimista / constraint de exclusión en BD para carreras concurrentes.
- OpenAPI/Swagger y colección Postman.
- Soft-delete de salas y auditoría (`createdBy`).
- Rate limiting y secret JWT vía variables de entorno.

## Uso de la IA

### Qué se pidió

1. Planificar el requerimiento siguiendo las reglas del repo, preguntar aclaraciones y esperar OK.
2. Decisiones: enfoque standalone (1A), JWT, H2, Flyway.
3. Implementar el plan completo (scaffold, dominio, auth, rooms, reservations, tests, README).

### Qué se aceptó

- Endpoints y contrato del plan (`POST /list`, JWT en mutaciones, 409 en solape).
- Soft cancel, H2 file, Flyway `V1__init.sql`.
- Refactor a CQRS alineado a `backend-spring.mdc` (handlers, domain puro, MapStruct).
- Tests unitarios del command handler + MockMvc de integración.

### Qué se corrigió / ajustó

- Se descartó Linear/GitHub/MySQL/`database_migrations` (fuera del enunciado standalone).
- Primera entrega usó capas `Service`; se corrigió a **CQRS** según rules.
- Lombok se omitió por incompatibilidad con JDK 25 local; handlers usan constructores explícitos.
- `HeadersDto` vía `@RequestHeader` en controllers.
- `UserDetailsConfig` separado de `SecurityConfig` (evitar ciclo con filtro JWT).
- En JDK 25: `-Dnet.bytebuddy.experimental=true` en Surefire para Mockito.
- Secret JWT dimensionado para HS256 (≥ 32 bytes).
