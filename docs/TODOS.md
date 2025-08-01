# TODOs

Tareas pendientes, para organizar los futuros features y commits.

---

## Gestión de usuarios

### Feature/toduserendpoints
- [X] Implementar `GET /users` — Obtener todos los usuarios
- [X] Implementar `DELETE /users/{id}` — Eliminar un usuario por ID

## TODOs - Inseguros

### Feature/creatodo
- [X] Implementar `POST /todos` — Crear un TODO (sin autenticación, entre comillas)
- [X] Validar datos que se envía durante la creación del TODO

### Feature/getodos
- [X] Implementar `GET /todos/{id}` — Obtener un TODO por ID
- [X] Implementar `GET /todos` — Obtener todo TODO (sin autenticación)
- [X] Añadir capacidad de filtrado en `GET /todos` (por usuario exacto o texto que encaje parcialmente)

---

## TODO - Seguro

### Feature/updatodo
- [X] Implementar `Patch /api/todos/{id}` — Actualizar un TODO (requiere autenticación)
- [X] Autorizar acceso a la actualización según propiedad del usuario

### Feature/eliminatodo
- [X] Implementar `DELETE /api/todos/{id}` — Eliminar un TODO (requiere autenticación)
- [X] Autorizar acceso a la eliminación según propiedad del usuario

---

## Seguridad y Autenticación

### Feature/finalsecuritodo
- [X] Finalizar endpoints de login, registro y logout
- [X] Integrar login/logout con manejo de sesión mediante Redis
- [X] Asegurar la validación de sesión en middleware para endpoints protegidos

---

## Frontend (Dividido por Plantillas)

### Feature/frontemplate
- [X] Definir plantilla HTML base
- [X] Incluir estructura y recursos compartidos (head, footer, etc.)

### Feature/prelogin
- [X] Dashboard: vista general de TODOs
- [X] Crear página de login
- [X] Crear página de registro
- [X] Crear vista de error/no autorizado

### Feature/poslogin
- [X] Página de creación/edición de TODOs
- [X] Botón de logout y feedback visual de sesión

---