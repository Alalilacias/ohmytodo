# 🧩 Documento de diseño: TODO Application

## TOC

## 1. Tecnologías seleccionadas

| Capa               | Tecnología                   |
| ------------------ |------------------------------|
| Backend            | Java 17, Spring Boot 3+      |
| ORM & Persistencia | Spring Data JPA, JPA API     |
| Base de datos      | PostgreSQL (o H2 para tests) |
| Seguridad (Bonus)  | Spring Security              |
| Tests              | JUnit 5, Mockito             |
| Frontend           | Thymeleaf                    |  

---

## 2. Estructura de carpetas

```
com.todoapp
├── controller
├── dto
├── model
│   ├── User
│   ├── Todo
│   └── Address (embedded)
├── repository
├── service
├── security (opcional)
└── exception
```

## 3. Modelo de datos

### Entidad: `User`

* `id`: `Long`
* `name`: `String`
* `username`: `String`
* `password`: `String`
* `address`: `@Embedded Address`

### Clase embebida: `Address`

* `street`: `String`
* `city`: `String`
* `zipcode`: `String`
* `country`: `String`

### Entidad: `Todo`

* `id`: `Long`
* `title`: `String`
* `completed`: `Boolean`
* `user`: `@ManyToOne User`

---

## 3. Endpoints

| Método | Endpoint          | Descripción                          |
| ------ | ----------------- | ------------------------------------ |
| GET    | `/api/users`      | Listar usuarios                      |
| POST   | `/api/users`      | Crear usuario                        |
| GET    | `/api/todos`      | Listar TODOs con filtros, orden, pag |
| GET    | `/api/todos/{id}` | Obtener un TODO (opcional)           |
| POST   | `/api/todos`      | Crear TODO                           |
| PUT    | `/api/todos/{id}` | Editar TODO (validar propiedad)      |
| DELETE | `/api/todos/{id}` | Eliminar TODO (validar propiedad)    |
| POST   | `/api/auth/login` | Autenticación (si aplicas seguridad) |