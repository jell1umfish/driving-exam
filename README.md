# Driving Exam Reservation API

REST API сервис для бронирования экзамена на водительское удостоверение.

## Технологии

- Java 17
- Spring Boot 4.1.0
- Spring Web
- Spring Data JPA
- PostgreSQL 15
- Flyway
- Docker & Docker Compose
- Swagger / OpenAPI

## Запуск проекта

### Требования
- Docker
- Docker Compose

### Команда запуска

```bash
docker compose up --build
```

После запуска приложение доступно на http://localhost:8080

Swagger UI доступен на http://localhost:8080/swagger-ui.html

## API

### Студенты `/api/v1/students`

| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | /api/v1/students | Создать студента |
| GET | /api/v1/students | Получить всех студентов |
| GET | /api/v1/students/{id} | Получить студента по ID |
| PUT | /api/v1/students/{id} | Обновить студента |
| DELETE | /api/v1/students/{id} | Удалить студента |

### Бронирования `/api/v1/reservations`

| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | /api/v1/reservations | Создать бронирование |
| GET | /api/v1/reservations | Получить все бронирования |
| GET | /api/v1/reservations/{id} | Получить бронирование по ID |
| PUT | /api/v1/reservations/{id} | Обновить бронирование |
| DELETE | /api/v1/reservations/{id} | Удалить бронирование |
| PATCH | /api/v1/reservations/{id}/cancel | Отменить бронирование |

## Конфигурация базы данных

| Параметр | Значение |
|----------|----------|
| Host | localhost |
| Port | 5432 |
| Database | driving_exam |
| Username | postgres |
| Password | postgres |

## Бизнес-правила

1. Студент с указанным ID должен существовать
2. Нельзя создать бронирование на дату в прошлом
3. У одного студента может быть только одно активное бронирование
4. Нельзя отменить бронирование со статусом COMPLETED
5. При создании бронирования статус автоматически устанавливается в ACTIVE

## Пагинация

Все списки поддерживают пагинацию через query parameters:

```
GET /api/v1/students?page=0&size=10
GET /api/v1/reservations?page=0&size=10
```