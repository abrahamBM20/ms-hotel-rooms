# ms-hotel-rooms
microservicio proyecto Cloud Native I

Oswell Andrade
Abraham Barrera

## Responsabilidad

Este servicio administra el catálogo de habitaciones y ofrece la lectura síncrona
de las reservas almacenadas en Supabase. El BFF es el único componente público:
valida Cognito, aplica los permisos y delega estas consultas por la red privada
de la EC2.

Las reservas no se crean desde este servicio. El flujo de escritura permanece
asíncrono:

```text
BFF → SNS → SQS → reservation-recorder → Supabase
```

## Endpoints internos

```text
GET /api/v1/rooms
GET /api/v1/rooms/{id}
POST /api/v1/rooms
PUT /api/v1/rooms/{id}
DELETE /api/v1/rooms/{id}

GET /api/v1/reservations?status=&roomNumber=&guestId=&id=&limit=
```

`GET /api/v1/reservations` consulta directamente la tabla `reservations` con
filtros parametrizados y un límite máximo de 500 resultados. No debe exponerse
directamente a internet; el BFF es quien filtra las reservas de los huéspedes
por su `sub` de Cognito.