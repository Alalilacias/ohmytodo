# TODOs

Tareas pendientes, para organizar los futuros features y commits.

---

## Gestión de usuarios

### Feature/toduserendpoints
- [X] Implementar `GET /users` — Obtener todos los usuarios
- [ ] Implementar `DELETE /users/{id}` — Eliminar un usuario por ID

## TODOs - Inseguros

### Feature/creatodo
- [ ] Implementar `POST /todos` — Crear un TODO (sin autenticación, entre comillas)
- [ ] Validar la entrada de creación del TODO

### Feature/getodos
- [ ] Implementar `GET /todos/{id}` — Obtener un TODO por ID
- [ ] Implementar `GET /todos` — Obtener todos los TODOs (sin autenticación)
- [ ] Añadir capacidad de filtrado en `GET /todos` (por categoría, prioridad, fecha, etc.)

---

## TODOs - Seguro

### Feature/updatodo
- [ ] Implementar `PUT /secure/todos/{id}` — Actualizar un TODO (requiere autenticación)
- [ ] Autorizar acceso a la actualización según propiedad del usuario

### Feature/eliminatodo
- [ ] Implementar `DELETE /secure/todos/{id}` — Eliminar un TODO (requiere autenticación)
- [ ] Autorizar acceso a la eliminación según propiedad del usuario

---

## Seguridad y Autenticación

### Feature/finalsecuritodo
- [ ] Finalizar endpoints de login, registro y logout
- [ ] Integrar login/logout con manejo de sesión mediante Redis
- [ ] Asegurar la validación de sesión en middleware para endpoints protegidos

---

## Frontend (Dividido por Plantillas)

### Feature/frontemplate
- [ ] Definir plantilla HTML base
- [ ] Incluir estructura y recursos compartidos (head, footer, estilos globales)

### Feature/prelogin
- [ ] Crear página de login
- [ ] Crear página de registro
- [ ] Crear vista de error/no autorizado

### Feature/poslogin
- [ ] Dashboard: vista general de TODOs
- [ ] Página de creación/edición de TODOs
- [ ] Página de configuración de usuario / perfil
- [ ] Botón de logout y feedback visual de sesión

---