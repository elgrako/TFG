# TFG — App Android de gestión de guardias jurídicas

Aplicación Android (Trabajo de Fin de Grado) para la gestión del turno de oficio: permite a los abogados registrar guardias, hacer seguimiento de su situación administrativa (presentado, validado, pagado), gestionar apelaciones y recursos, y recibir notificaciones. Es el cliente móvil del backend [`tfg-api`](../tfg-api).

## Funcionalidades

- Registro e inicio de sesión de usuarios.
- Alta, edición y consulta de guardias (turnos), con filtros por juzgado, nombre o estado de cobro.
- Seguimiento del estado de cada guardia: presentación, validación y pago.
- Gestión de apelaciones, recursos y recursos extraordinarios asociados a una guardia.
- Notificaciones locales.
- Caché local en SQLite para trabajar con los datos ya consultados.
- Comunicación en tiempo real vía WebSocket con el servidor.

## Tecnologías

- Android nativo en Java (minSdk 26, compileSdk 35).
- Retrofit 2 + Gson para el consumo de la API REST.
- OkHttp (interceptor para adjuntar el token JWT en cada petición).
- SQLite (`SQLiteOpenHelper`) para persistencia local.
- WebSocket para eventos en tiempo real.

## Estructura

- `LoginActivity`, `RegisterActivity` — autenticación.
- `MainActivity`, `MainGuardiaActivity`, `GuardiaActivity` — pantallas principales de guardias.
- `SituacionGuardiaActivity`, `ApelacionGuardiaActivity`, `RecursoGuardiaActivity`, `RecursoExtraOrdinarioActivity` — gestión del estado y los recursos de una guardia.
- `WebSocketActivity` — eventos en tiempo real.
- `api/` — cliente Retrofit y definición de endpoints (`ApiService`).
- `entities/` — modelos de datos (Guardia, Usuario, Registro, etc.).
- `Helpers/` — utilidades (base de datos local, notificaciones, preferencias, mensajes).

## Cómo ejecutarlo

Proyecto estándar de Android Studio (Gradle). Requiere que el backend (`tfg-api`) esté accesible; la URL base se configura en `RetrofitClient`.

## Notas técnicas

La URL del backend está fijada en el código (`RetrofitClient.BASE_URL`) apuntando a una IP concreta por HTTP, no HTTPS. Para una versión más allá del entorno académico, lo recomendable sería mover esa URL a configuración de build (`BuildConfig`) y servir la API sobre HTTPS.
