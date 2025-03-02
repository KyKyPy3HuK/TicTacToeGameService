Веб приложение умеющее играть в крестики нолики со следующими свойствами:

1. Приложение работает на версии Java 17 с использованием SpringBoot
2. Приложение умеет вести несколько партий одновременно, партия пользователя идентифицируется параметром который записывается в сессию пользователя
3. Приложение использует JPA Hibernate, каждый ход приложения сохраняется в h2:mem БД.
4. Подробное описание команд API в формате OpenAPI 3.0.0 находится в файле openapi.yaml
   Основые возможности кратко:
    - Начать новую партию
    - Сделать ход (указывается позиция pos в диапазоне от 0 до 8 по формуле pos = y * 3 + x (клетки доски начиная с левой верхней нулевой заканчивая правой нижней восьмой, напрвление счета - слева-направо сверху-вниз)
    - Отменить ход
    - Дать текущую доску
5. Приложение автоматически запускает задание по расписанию раз в сутки, и стирает из БД доски, последний ход от пользователя на которых был более суток назад
6. Параметры приложения в env переменных
   - util.autocleaner.cron - устанавливает время и период очистки старых досок в формате cron
   - util.computer-ai.use-random-mode - устанавливает режим искусственного интеллекта машины, если true - случайная стратегия ходов, false - машина использует оптимальную выйгрышную стратегию
7. Сборщик приложения maven
8. Имеется Dockerfile для контейнеризации приложения на базе JDK eclipse-temurin
