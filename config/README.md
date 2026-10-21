# ===========================================================================
#  config/ — fuente de verdad del secreto del stack
# ===========================================================================
#
#  `secrets.env` es el archivo REAL (con los valores de este entorno) y está
#  en .gitignore: no se commitea nunca.
#  `secrets.env.example` es la plantilla con la estructura y los valores por
#  defecto; esa sí se commitea.
#
#  ## Qué contiene
#
#  El manifiesto completo de variables de entorno de los cuatro proyectos del
#  stack (frontend, BFF, ms-hotel-rooms y las Lambdas), agrupadas por dueño.
#  Los valores del frontend son los canónicos: si cambia el pool de Cognito o
#  el API Gateway, se actualiza primero el `.env` del frontend y después esta
#  plantilla.
#
#  ## Por qué vive en este repo
#
#  Porque `ms-hotel-rooms` es el microservicio que DEFINIÓ el contrato de la
#  cabecera `X-Secret-Gateway` (ver SecretGatewayFilter). El BFF, el API
#  Gateway y las Lambdas tienen que hablar el mismo idioma, y ese idioma se
#  documenta en un solo archivo.
#
#  ## Cómo usarlo en local
#
#  bash / git-bash:
#
#      cp config/secrets.env.example config/secrets.env
#      set -a && . config/secrets.env && set +a
#      cd ../ms-hotel-rooms && ./gradlew bootRun
#
#  PowerShell:
#
#      Copy-Item config/secrets.env.example config/secrets.env
#      Get-Content config/secrets.env | Where-Object { $_ -match '=' -and $_ -notmatch '^#' } |
#        ForEach-Object { $n,$v = $_ -split '=',2; Set-Item -Path "Env:$n" -Value $v }
#      cd ../ms-hotel-rooms ; .\gradlew.bat bootRun
#
#  ## Cómo se replica en AWS
#
#  El valor real de `API_GATEWAY_SECRET` tiene que quedar en tres sitios, y
#  tiene que ser EXACTAMENTE el mismo:
#
#    1. Stage variable del API Gateway (la inyecta como cabecera X-Secret-Gateway).
#    2. Env var del contenedor del BFF.
#    3. Env var del contenedor de ms-hotel-rooms.
#
#  Si uno de los tres se queda atrás, la cadena responde 403 y no se nota
#  hasta que se prueba el flujo completo. Por eso conviene deployear siempre
#  el API Gateway y el BFF en la misma ventana.
#
#  ## Capa añadida de seguridad
#
#  El secreto compartido es la segunda capa. La primera es que
#  `ms-hotel-rooms` NO tiene ruta pública en el API Gateway: solo el BFF
#  llega a él, por red interna, reenviando la cabecera. Si alguien llega
#  directamente contra ms-hotel-rooms desde internet, no llega.
#
#  Para aislar todavía mas, se puede definir `ROOMS_GATEWAY_SECRET` con un
#  valor distinto al del API Gateway: asi, comprometer el secreto del gateway
#  no da acceso directo al microservicio. El BFF acepta ambas variables y usa
#  `ROOMS_GATEWAY_SECRET` para hablar con ms-hotel-rooms.
