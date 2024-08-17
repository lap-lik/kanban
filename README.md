# Kanban
![image](https://github.com/user-attachments/assets/b61d6f0b-1c05-4dbc-8061-46a7859c4c29)


## Описание проекта

Трекер задач представляет собой бэкенд часть веб-сервисного монолитного приложения для управления задачами на основе канбан-доски. Приложение предназначено для организации совместной работы над задачами, позволяя пользователям выполнять операции создания, чтения, обновления и удаления.

### Основные возможности:
- **Типы задач:** общие задачи, подзадачи и эпики. Эпики могут содержать несколько подзадач.
- **Сохранение данных:** в оперативной памяти, в файл на локальной машине (формат CSV) или на сервере.
- **Сетевое взаимодействие:** отправка и получение данных с сервера с помощью библиотек Gson и HttpServer.
- **Серверная архитектура:** два сервера — один для управления процессами сервера (приём, отправка, старт, остановка), другой для внутренней реализации логики.
- **Тестирование:** покрытие функциональности с помощью JUnit тестов.

## Функционал:

Программа предлагает три ключевых способа хранения данных:
1. **В оперативной памяти на локальной машине.**
2. **В файле на локальной машине (CSV формат).**
3. **На сервере с использованием клиента.**

## Стек технологий:

- **Java Core:**
  - **ООП (Объектно-ориентированное программирование):**
    - Методы и классы: инкапсуляция, наследование и полиморфизм.
    - Принципы: DRY (не повторяйся), сокрытие полей, переопределение методов.
    - Стандартные классы: `Object`, методы `equals`, `hashCode`, `toString`, работа с код-стилем и областями видимости.
- **Git:**
  - Команды: `add`, `commit`, `status`, `branch`, `checkout`, `merge` и другие для контроля версий.
- **Алгоритмы и структуры данных:**
  - Java Collections Framework: `List`, `Set`, `Map`, сортировки и алгоритмы поиска.
  - Регулярные выражения для обработки строк.
- **Исключения:**
  - Обработка исключений с помощью `try-catch-finally`, иерархия исключений и работа со стек-трейсом.
- **Работа с файлами:**
  - Обработка файлов с помощью классов `File`, `Files`, `Path`, стримов и кодировок.
- **Функциональное программирование:**
  - Функциональные интерфейсы (`Consumer`, `Supplier`, `Function`), лямбда-выражения и работа с дженериками.
- **Сетевое взаимодействие:**
  - Клиент-серверная модель и сетевые протоколы.
  - Работа с API (REST), сериализация данных в формате JSON с использованием библиотеки GSON.

