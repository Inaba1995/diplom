# Дипломный проект по профессии «Тестировщик»

Проект реализует набор тестов и содержит отчёты по проведённым тестам для веб-сервиса, который предлагает купить тур по определённой цене двумя способами:

* Обычная оплата по дебетовой карте.
* Уникальная технология: выдача кредита по данным банковской карты.

**План автоматизации**

[Plan.md](https://github.com/Inaba1995/diplom/tree/master/docs/Plan.md)

**Отчётные документы по итогам тестирования**

[Report.md](https://github.com/Inaba1995/diplom/tree/master/docs/Report.md)

**Отчётные документы по итогам автоматизации**

[Summary.md](https://github.com/Inaba1995/diplom/tree/master/docs/Summary.md)

## Начало работы

С помощью Git CLI клонировать репозиторий целиком в директорию на свой компьютер команды
```
git clone https://github.com/Inaba1995/diplom
```

### Prerequisites

* Git CLI
* OpenJDK 11
* Intellij IDEA Community Edition, дополнительные плагины: Docker, Lombok.
* Google Chrome
* Docker с плагином docker-compose

### Установка и запуск
1. Открыть терминал в корневой директории проекта.
1. Необходимо загрузить и запустить контейнеры docker, содержащие эмулятор платёжного сервиса, СУБД MySQL и PostgreSQL в терминале с помощью команды 

    ```
    docker compose up
    ```
1. Запустить тестируемое приложение
   * с использованием СУБД PostgreSQL в терминале с помощью команды:
    ```
    java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar
    ```
   * с использованием СУБД MySQL в терминале с помощью команды:
   ```
   java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar
   ```
1. Запустить на выполнение тесты из файла src/test/java/Test.java.

1. Запустить автотесты одной из команд в зависимости от выбранной СУБД:
   * с использованием СУБД PostgreSQL в терминале с помощью команды:
    ```
    gradlew test -Dtest.dburl=jdbc:postgresql://localhost:5432/app
    ```
   * с использованием СУБД MySQL в терминале с помощью команды:
    ```
    gradlew test -Dtest.dburl=jdbc:mysql://localhost:3306/app
    ```
1. Остановить тестируемое приложение в терминале, в котором оно запущено сочетанием клавиш:

    ```
    CTRL + C
    ```
1. Остановить все сервисы Docker, запущенные с помощью конфигурации docker-compose командой:
    ```
    docker-compose down
    ```
## Лицензия

Свободно для любого использования, копирования и изменения.