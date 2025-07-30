<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a id="readme-top"></a>
<br />

<div align="center">
  <a href="https://github.com/Alalilacias/ohmytodo/blob/main/src/main/resources/static/images/todo_icon.png">
    <img src="src/main/resources/static/images/todo_icon.png" alt="Logo" width="80" height="80">
  </a>
<h3 align="center">OhMyTodo</h3>
</div>

## Acerca del proyecto

Este README fue hecho basándome en una plantilla que he visto en uso en mis proyectos FOSS favoritos. Le he quitado muchos detalles que creo que no son necesarios para un proyecto hecho para una entrevista de trabajo, pero he dejado algunas cosas que me gustaban.

El proyecto sigue las especificaciones facilitadas por correo, que se pueden revisar en la carpeta de docs, bajo el nombre `omc_prueba_tecnica_pring.pdf`. Así, siguiendo estas especificaciones, se escogieron Spring Boot y Thymeleaf como combinación de Backend y Frontend.

Como base de datos, se utilizó PostgreSQL, por tener experiencia en proyectos propios con ella y por su versatilidad y su característica relacional. Así mismo, si bien mi experiencia me llevaba más hacia el uso de JWT para la seguridad, escogí Redis y Spring Session para la seguridad de este proyecto, con endpoints customizados y una gestión del usuario que no requiere el guardado de un token en el navegador del usuario, sino una cookie que el servidor se encarga de gestionar y resolver. Si bien esto requiere más memoria por parte del servidor, mejora mucho la experiencia del usuario final.

La documentación se ha hecho primordialmente mediante un seguimiento de buenas prácticas de nombres (aunque debo mencionar que sé generar documentación javadoc, pero no quise hacer poco agradable a la vista el repositorio) y SpringDoc OpenAPI.

En cuanto al testing, se ha hecho utilizando Mockito, JUnit 5 y H2 para evitar iniciar el repositorio cada vez que se hicieran pruebas. Así mismo, todos los endpoints fueron probados mediante Postman y organizados en una colección el JSON de la cual es parte de este repositorio, por lo que se puede descargar, importar a JSON y ejecutar casi inmediatamente.

Por último, para mayor portabilidad, el proyecto se preparó de tal manera que el único requerimiento para utilizarlo sea Docker. Docker compose es necesario también, en sistemas Linux, pero en un sistema Windows debería bastar con instalar Docker Desktop.

Ha sido una excelente experiencia de proyecto y estoy abierto a cualquier pregunta que puedan tener sobre él.

### Tecnologías utilizadas

- [![Spring Boot][SpringBoot-badge]][SpringBoot-url]
- [![Gradle][Gradle-badge]][Gradle-url]
- [![Redis][Redis-badge]][Redis-url]
- [![PostgreSQL][PostgreSQL-badge]][PostgreSQL-url]
- [![Docker][Docker-badge]][Docker-url]
- [![Docker Compose][DockerCompose-badge]][DockerCompose-url]

## Preparación para ejecución

En esta sección está la información sobre los requisitos y el proceso para ejecutar este proyecto en un entorno local, ha sido probado en Windows y Linux, aunque debería funcionar en un Mac, asumiendo la existencia de Docker y Docker Compose.

### Requisitos previos

Es importante asegurar la instalación de Docker y Docker Compose en el sistema. Si ya tiene instalado, configurado y encendido el servicio de Docker, ignore este punto. Así pues:

#### En Linux

- [Docker Engine](https://docs.docker.com/engine/install/)
- [Docker Compose V2+](https://docs.docker.com/compose/install/)

Para verificar:

```bash
docker -v
docker compose version
```

#### En Windows

- [Docker Desktop](https://www.docker.com/products/docker-desktop) (incluye Docker Engine y Docker Compose)
- Activar WSL 2 (recomendado para Windows 10/11)

Para verificar desde PowerShell:

```powershell
docker -v
docker compose version
```

### Instalación

La instalación consiste de tres pasos: clonar el repositorio de GitHub, entrar a él e iniciar su construcción mediante docker compose.

1. Clone el repositorio:

   #### Linux (terminal) y Windows (PowerShell)

   ```bash
   git clone https://github.com/github_username/ohmytodo.git
   cd ohmytodo
   ```

2. Construya e inicie los contenedores:

   #### Linux y Windows

   ```bash
   docker compose up --build
   ```

   Esto iniciará tres servicios:
   - La aplicación en `http://localhost:8080`
   - Redis en `localhost:6379`
   - PostgreSQL en `localhost:5432` (usuario: `postgres`, contraseña: `postgres`)

### Verificación

Una vez iniciado todo correctamente:

- Abra su navegador y acceda a: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) donde podrá leer la documentación de swagger del repositorio.

## Pruebas en el proyecto

El desarrollo de esta aplicación se ha basado en decisiones tomadas a partir de experiencias previas, por lo que se buscó mantener una API REST robusta como backend, que responde mediante `ResponseEntity` y entrega datos en formato JSON, pero adaptada a los requisitos de la prueba técnica de utilizar una interfaz con **Thymeleaf**, que aprovecha JavaScript en el navegador para adaptar las peticiones a los endpoints de la API según las necesidades del frontend.

Si bien en un entorno de producción se utilizaría otro Frontend capaz de interactuar con un Backend de API REST o se utilizaría de manera correcta un modelo MVC, en este caso se ha optado por un modelo mixto para mostrar mis capacidades y para ceñirme a los requisitos de la prueba.

### 🧪 Pruebas con Postman

La API puede ser probada directamente mediante [Postman](https://www.postman.com/), utilizando una colección preparada específicamente para este fin. Para hacerlo:

1. Abre Postman, entre a un workspace y localice el botón de `Import` a la derecha de las colecciones.
2. Diríjase a la ruta `src/test/postman/` del proyecto.
3. Descargue el archivo `OhMyTodo.postman_collection.json`.
4. En Postman, haz clic en **Import** y seleccione o arrastre el archivo copiado.
5. Ejecuta todas las peticiones disponibles para probar el comportamiento de los endpoints (registro de usuarios, login, creación de tareas, etc.).

### 🌐 Pruebas desde el navegador

También se puede interactuar con la aplicación desde la interfaz web:

1. Asegúrese de que la aplicación esté corriendo (`http://localhost:8080`).
2. Abra su navegador y acceda a [http://localhost:8080/index](http://localhost:8080/index).
3. Desde allí podrás usar todas las funcionalidades disponibles como usuario registrado (registro, login, creación y visualización de tareas).
4. Algunas peticiones requieren autorización de usuario, pero el sistema se encargará de aclarar cuáles son cuando se realicen de manera errónea.

[SpringBoot-badge]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot

[Gradle-badge]: https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[Gradle-url]: https://gradle.org/

[Redis-badge]: https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white
[Redis-url]: https://redis.io/

[PostgreSQL-badge]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/

[Docker-badge]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/

[DockerCompose-badge]: https://img.shields.io/badge/Docker--Compose-384d54?style=for-the-badge&logo=docker&logoColor=white
[DockerCompose-url]: https://docs.docker.com/compose/
[SpringBoot-badge]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot

[Gradle-badge]: https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[Gradle-url]: https://gradle.org/

[Redis-badge]: https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white
[Redis-url]: https://redis.io/

[PostgreSQL-badge]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/

[Docker-badge]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/

[DockerCompose-badge]: https://img.shields.io/badge/Docker--Compose-384d54?style=for-the-badge&logo=docker&logoColor=white
[DockerCompose-url]: https://docs.docker.com/compose/