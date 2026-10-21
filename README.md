# ms-hotel-rooms

Microservicio del catálogo de habitaciones del Hotel Boutique 360.

Oswell Andrade
Abraham Barrera

---

## Qué es

Un CRUD de habitaciones sobre la tabla `rooms`, con su propia seguridad:

| Ruta | Acceso |
|---|---|
| `GET /api/health` | público |
| `GET /api/v1/rooms` | público (pero exige `X-Secret-Gateway`) |
| `GET /api/v1/rooms/{id}` | público (pero exige `X-Secret-Gateway`) |
| `POST /api/v1/rooms` | rol `ADMIN` |
| `PUT /api/v1/rooms/{id}` | rol `ADMIN` |
| `DELETE /api/v1/rooms/{id}` | rol `ADMIN` (soft delete) |

**No depende del BFF, ni de SNS, ni de SQS, ni de las Lambdas.** Se levanta y
se prueba solo: necesita únicamente PostgreSQL y Cognito. El BFF lo invoca por
HTTP y devuelve su JSON tal cual.

Lo único que comparte con el resto del stack es la **tabla `rooms`**: las
Lambdas actualizan `is_available` ahí, así que microservicio y Lambdas deben
apuntar a la **misma** base de datos.

---

## Variables de entorno

| Variable | Para qué | Default |
|---|---|---|
| `PORT` | Puerto del contenedor | `8080` |
| `PUBLIC_HOST_URL` | Host público (informativo) | — (obligatorio) |
| `COGNITO_ISSUER_URI` | Pool de Cognito que valida el JWT | `us-east-1_rf0QM9OSc` |
| `API_GATEWAY_SECRET` | Secreto de la cabecera `X-Secret-Gateway` | — (obligatorio) |
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | PostgreSQL (tabla `rooms`) | — (obligatorio) |

> El manifiesto completo de variables del stack (frontend + BFF + microservicio +
> Lambdas) está en [`config/secrets.env.example`](config/secrets.env.example), y
> las instrucciones de uso en [`config/README.md`](config/README.md).

---

## El secreto de la pasarela

Todas las rutas exigen la cabecera `X-Secret-Gateway` (salvo `/api/health`):

```
curl -H "X-Secret-Gateway: $API_GATEWAY_SECRET" http://localhost:8081/api/v1/rooms
```

Quien la envía:

- El **API Gateway** hacia el BFF.
- El **BFF** hacia este microservicio, reenviando **su** secreto (no el que
  recibió del cliente) — así el BFF es una capa añadida de seguridad y nadie
  puede hacerse pasar por el gateway.

Por eso `ms-hotel-rooms` **no debe tener ruta propia en el API Gateway**: solo
el BFF llega a él, por red interna.

> **El log muestra el secreto a propósito.** Cada request imprime
> `DEBUG SECRETO -> Ruta: ... | Recibido: [...] | Esperado: [...]` para
> acordarse del valor mientras se configura el stack (es una entrega de
> clases). En producción esa línea se borra, porque escribe el secreto en
> claro en los logs.

---

## Levantar en local

```bash
# 0. Cargar las variables (el manifiesto vive en config/)
cp config/secrets.env.example config/secrets.env     # y edítalo
set -a && . config/secrets.env && set +a

# 1. Arrancar
PORT=8081 ./gradlew bootRun
```

---

## Compilar

```bash
./gradlew build
./gradlew bootRun
docker build -t ms-hotel-rooms .
```
