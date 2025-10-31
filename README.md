# Инструкция по сборке и запуску проекта

## Предварительные требования

- Установлен Docker и Docker Compose
- Клонирован репозиторий этого проекта

## Шаги запуска

1. Клонируйте репозиторий:
```bash
git clone https://github.com/niccneimi/link-cutter
cd link-cutter
```

2. Создайте файл `.env` в корне репозитория и заполните необходимыми переменными окружения
```
DB_HOST=db
DB_PORT=5432
POSTGRES_DB=mydatabase
POSTGRES_USER=myuser
POSTGRES_PASSWORD=securepassword
SHORT_LINK_ID_LENGTH=10
APP_DOMAIN_NAME=lc.ru.tuna.am
```
3. Запустите приложение и базу данных с помощью Docker Compose:
```bash
docker compose up -d --build
```

4. Проверьте, что контейнеры работают:
```bash
docker compose ps
```

5. При необходимости остановите контейнеры:
```bash
docker compose down
```
## Пример использования

#### Создание короткой ссылки
```bash
curl -X POST -H "Content-Type: application/json" -d '{"link":"https://www.youtube.com/watch?v=dQw4w9WgXcQ"}' https://lc.ru.tuna.am/api/link

```
#### Переход по сокращенной ссылке

https://lc.ru.tuna.am/s8omr8pozg

## Особенности

- Конфигурация Spring Boot подхватывает переменные из `.env`.
- Данные PostgreSQL сохраняются в volume `db_data`, поэтому данные будут сохранены между перезапусками, что даёт сохраение состояния сервиса.
