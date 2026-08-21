# Explore With Me

Explore With Me - это backend-приложение для публикации событий и поиска компании для участия в них. Пользователи могут создавать события, просматривать опубликованные мероприятия, отправлять заявки на участие, подтверждать или отклонять заявки к своим событиям, а администратор может управлять пользователями, категориями, событиями и подборками.

Проект состоит из основного сервиса и отдельного сервиса статистики, который сохраняет информацию о просмотрах публичных эндпоинтов и позволяет получать количество просмотров событий.

## Описание проекта

Приложение реализовано как многомодульный Maven-проект:

- `ewm-main-service` - основной сервис приложения;
- `ewm-stat-service` - сервис статистики;
- `ewm-stats-client` - общий клиентский модуль для взаимодействия со статистикой.

## Технологии

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Boot Actuator
- Hibernate
- PostgreSQL
- Liquibase
- Maven
- Docker
- Docker Compose
- Lombok
- Checkstyle
- SpotBugs

## Архитектура проекта

```text
explore-with-me/
├── ewm-main-service/          # Основной сервис приложения
│   ├── controller/            # REST-контроллеры
│   ├── service/               # Бизнес-логика
│   ├── repository/            # Работа с базой данных
│   ├── model/                 # JPA-сущности
│   ├── dto/                   # DTO для API
│   ├── mapper/                # Маппинг сущностей и DTO
│   ├── exception/             # Обработка ошибок
│   ├── validation/            # Валидация данных
│   └── resources/db/changelog # Liquibase-миграции
│
├── ewm-stat-service/          # Сервис статистики
│   ├── controller/            # API статистики
│   ├── service/               # Логика сбора статистики
│   ├── repository/            # Запросы к базе статистики
│   ├── model/                 # Сущности статистики
│   ├── mapper/                # Маппинг статистики
│   └── resources/db/changelog # Liquibase-миграции
│
├── ewm-stats-client/          # Общий модуль статистики
│   ├── client/                # HTTP-клиент статистики
│   └── dto/                   # DTO статистики
│
├── docker-compose.yml
└── pom.xml
```

## Модули

### ewm-main-service

Основной сервис содержит API приложения.

Функциональность:

- управление пользователями;
- управление категориями;
- создание и редактирование событий;
- модерация событий администратором;
- поиск опубликованных событий;
- подача заявок на участие;
- подтверждение и отклонение заявок;
- управление подборками событий;
- получение статистики просмотров событий.

Сервис запускается на порту:

```text
8080
```

### ewm-stat-service

Сервис статистики отвечает за сохранение и получение информации о посещениях.

Функциональность:

- сохранение информации о запросе к endpoint;
- получение статистики по диапазону дат;
- фильтрация статистики по URI;
- подсчет обычных и уникальных просмотров.

Сервис запускается на порту:

```text
9090
```

### ewm-stats-client

Общий модуль для работы со статистикой.

Содержит:

- `EndpointHitDto` - DTO для сохранения информации о запросе;
- `ViewStatsDto` - DTO для получения статистики;
- `StatsClient` - HTTP-клиент для обращения из основного сервиса к сервису статистики.

Модуль не является самостоятельным Spring Boot-приложением.

## Запуск проекта

### Требования

- Java 21
- Maven 3.8+
- Docker
- Docker Compose

### Запуск через Docker Compose

```powershell
docker compose up --build
```

После запуска сервисы будут доступны:

```text
Main Service:  http://localhost:8080
Stats Service: http://localhost:9090
```

Остановить контейнеры:

```powershell
docker compose down
```

Остановить контейнеры и удалить volume с базами данных:

```powershell
docker compose down -v
```

Команда `down -v` полезна, если нужно пересоздать базы и заново применить Liquibase-миграции.

## Сборка проекта

Собрать все модули:

```powershell
mvn clean install
```

Собрать без запуска тестов:

```powershell
mvn clean package -DskipTests
```

Собрать основной сервис вместе с зависимостями:

```powershell
mvn -pl ewm-main-service -am package
```

Собрать сервис статистики вместе с зависимостями:

```powershell
mvn -pl ewm-stat-service -am package
```

Запустить проверки качества кода:

```powershell
mvn -Pcheck install
```

## Конфигурация

### Main Service

Основные переменные окружения:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://ewm-db:5432/ewm_main
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
STATS_SERVER_URL=http://stats-server:9090
```

### Stats Service

Основные переменные окружения:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://stats-db:5432/ewm_stats
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

## API основного сервиса

### Public API

Публичные эндпоинты доступны без авторизации.

```text
GET /categories
GET /categories/{catId}

GET /compilations
GET /compilations/{compId}

GET /events
GET /events/{id}
```

Особенности публичного поиска событий:

- возвращаются только опубликованные события;
- текстовый поиск выполняется по аннотации и описанию без учета регистра;
- если диапазон дат не указан, возвращаются только будущие события;
- каждое событие содержит количество подтвержденных заявок и просмотров;
- информация о публичных запросах сохраняется в сервисе статистики.

### Private API

Эндпоинты для пользователей.

```text
GET /users/{userId}/events
POST /users/{userId}/events
GET /users/{userId}/events/{eventId}
PATCH /users/{userId}/events/{eventId}

GET /users/{userId}/events/{eventId}/requests
PATCH /users/{userId}/events/{eventId}/requests

GET /users/{userId}/requests
POST /users/{userId}/requests
PATCH /users/{userId}/requests/{requestId}/cancel
```

### Admin API

Эндпоинты администратора.

```text
GET /admin/users
POST /admin/users
DELETE /admin/users/{userId}

POST /admin/categories
PATCH /admin/categories/{catId}
DELETE /admin/categories/{catId}

GET /admin/events
PATCH /admin/events/{eventId}

POST /admin/compilations
PATCH /admin/compilations/{compId}
DELETE /admin/compilations/{compId}
```

## API сервиса статистики

### Сохранение информации о запросе

```http
POST /hit
```

Пример тела запроса:

```json
{
  "app": "ewm-main-service",
  "uri": "/events/1",
  "ip": "127.0.0.1",
  "timestamp": "2026-08-21 12:00:00"
}
```

### Получение статистики

```http
GET /stats
```

Пример запроса:

```http
GET /stats?start=2026-08-21%2000%3A00%3A00&end=2026-08-22%2000%3A00%3A00&uris=/events/1&unique=true
```

Параметры:

- `start` - начало диапазона;
- `end` - конец диапазона;
- `uris` - список URI, необязательный параметр;
- `unique` - учитывать только уникальные IP, по умолчанию `false`.

Пример ответа:

```json
[
  {
    "app": "ewm-main-service",
    "uri": "/events/1",
    "hits": 1
  }
]
```

## Модель данных

Основные сущности основного сервиса:

- `User` - пользователь;
- `Category` - категория события;
- `Event` - событие;
- `Location` - координаты события;
- `Request` - заявка на участие;
- `Compilation` - подборка событий.

Основные сущности сервиса статистики:

- `App` - приложение, отправившее информацию о запросе;
- `Hit` - сохраненный запрос к endpoint.

## Статусы

Статусы событий:

```text
PENDING
PUBLISHED
CANCELED
```

Статусы заявок:

```text
PENDING
CONFIRMED
REJECTED
CANCELED
```

## Миграции базы данных

Для управления схемой базы данных используется Liquibase.

Главный changelog основного сервиса:

```text
ewm-main-service/src/main/resources/db/changelog/db.changelog-master.yaml
```

Главный changelog сервиса статистики:

```text
ewm-stat-service/src/main/resources/db/changelog/db.changelog-master.yaml
```

## Обработка ошибок

Ошибки возвращаются в едином формате:

```json
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Описание ошибки",
  "timestamp": "2026-08-21 12:00:00"
}
```

Для разных ситуаций используются соответствующие HTTP-статусы:

- `400 BAD_REQUEST` - некорректный запрос;
- `404 NOT_FOUND` - объект не найден;
- `409 CONFLICT` - нарушение условий выполнения операции или целостности данных;
- `500 INTERNAL_SERVER_ERROR` - внутренняя ошибка сервера.

## Особенности реализации

- Проект разделен на несколько Maven-модулей.
- Основной сервис не хранит просмотры как источник истины, а получает их из сервиса статистики.
- Для публичных запросов к событиям сохраняется информация о посещении.
- Для подсчета уникальных просмотров используется IP пользователя.
- Схема базы данных управляется через Liquibase.
- Dockerfiles используют multi-stage build, поэтому приложение можно пересобирать командой `docker compose up --build`.

## Проверка

Проект проверяется автоматическими Postman-тестами и CI-пайплайном.

Перед отправкой изменений рекомендуется выполнить:

```powershell
mvn clean install
docker compose up --build
```
