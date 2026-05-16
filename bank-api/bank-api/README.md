# bank-api

Proyecto de ejemplo de API bancaria construido con Spring Boot, Spring Data JPA y PostgreSQL.

## Estructura del proyecto

```
src/main/java/com/example/bankapi/
├── controller/     # Controladores REST (endpoints de la API)
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos con Spring Data JPA
├── model/          # Entidades JPA
└── dto/            # Objetos de transferencia de datos
```

## Requisitos

- Java 21
- Maven 3.x
- Docker y Docker Compose (opcional, para ejecutar en contenedores)

## Configuración

La aplicación se configura en `src/main/resources/application.yml`. Los valores por defecto pueden sobreescribirse con variables de entorno:

| Variable                   | Valor por defecto                        |
|----------------------------|------------------------------------------|
| `SPRING_DATASOURCE_URL`    | `jdbc:postgresql://postgres:5432/bankdb` |
| `SPRING_DATASOURCE_USERNAME` | `postgres`                             |
| `SPRING_DATASOURCE_PASSWORD` | `postgres`                             |
| `SERVER_PORT`              | `8081`                                   |

Para personalizar sin modificar el YAML, copia `.env.example` a `.env` y ajusta los valores antes de levantar los servicios.

## Ejecutar localmente con Maven

1. Compilar el proyecto:

```bash
./mvnw clean package
```

2. Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

3. La API estará disponible en:

```
http://localhost:8081
```

## Ejecutar con Docker Compose

> Opción recomendada: levanta la base de datos y la API en un solo paso.

1. Compilar el JAR:

```bash
./mvnw clean package
```

2. Iniciar los servicios:

```bash
docker compose up --build
```

3. Detener los servicios:

```bash
docker compose down
```

Se levantan los siguientes contenedores:

| Contenedor      | Descripción                     | Puerto |
|-----------------|---------------------------------|--------|
| `postgres_bank` | Base de datos PostgreSQL        | `5432` |
| `bank_api`      | API Spring Boot                 | `8081` |

## Ejecutar solo la imagen Docker

1. Crear el JAR:

```bash
./mvnw clean package
```

2. Construir la imagen:

```bash
docker build -t bank-api .
```

3. Ejecutar el contenedor:

```bash
docker run --rm -p 8081:8081 --name bank-api bank-api
```

> **Nota:** el `application.yml` apunta a `postgres:5432` como host de la base de datos. Al correr el contenedor de forma aislada necesitas una instancia de PostgreSQL accesible con ese nombre. La opción recomendada es usar `docker compose up --build`.

## Pruebas

```bash
./mvnw test
```

## Endpoints principales

La API se expone en el puerto `8081`. La documentación interactiva está disponible en:

```
http://localhost:8081/swagger-ui/index.html
```