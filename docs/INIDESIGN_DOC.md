# Documento de diseño: TODO Application

Este documento existe con el propósito de facilitarme revisar el servicio que voy a crear, durante el trabajo con este.

La idea es simular el trabajo en la empresa y dentro de un sistema en el que se dejan unas bases sentadas en un documento. Imita el que seguíamos en Reformastic entre nosotros.

## TOC

1. [Tecnologías seleccionadas](#1-tecnologías-seleccionadas)
2. [Estructura de carpetas](#2-estructura-de-carpetas)
3. [Modelo de datos](#3-modelo-de-datos)
   - 3.1. [Entidad: User](#entidad-user)
   - 3.2. [Clase embebida: Address](#clase-embebida-address)
   - 3.3. [Entidad: Todo](#entidad-todo)
4. [Endpoints](#4-endpoints)


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

| Método | Endpoint           | Descripción                          | Seguridad |
|--------|--------------------|--------------------------------------|-----------| 
| GET    | `/api/users`       | Listar usuarios                      | No        |
| POST   | `/api/users`       | Crear usuario                        | No        |
| GET    | `/api/todos`       | Listar TODOs con filtros, orden, pag | No        |
| GET    | `/api/todos/{id}`  | Obtener un TODO                      | No        |
| POST   | `/api/todos`       | Crear TODO                           | No        |
| PUT    | `/api/todos/{id}`  | Editar TODO                          | Sí        |
| DELETE | `/api/todos/{id}`  | Eliminar TODO                        | Sí        |
| POST   | `/api/auth/login`  | Autenticación                        | No        |
| POST   | `/apo/auth/logout` | Desautenticación                     | Sí        |