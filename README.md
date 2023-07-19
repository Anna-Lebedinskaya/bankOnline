# bankOnline

Web-приложение "банк-онлайн".

Стек технологий:
- Java Core;
- Spring DATA (JPA/Hibernate);
- Spring MVC;
- Spring Boot;
- Spring Security;
- Thymeleaf;
- SQL (PostgreSQL);
- Docker.

Схема БД:
![bankProject](https://github.com/Anna-Lebedinskaya/bankOnline/assets/112424502/a158dc8a-2017-47cd-bf2c-57998382c489)

Предполагает три роли: админ, менеджер, клиент.

Менеджер может:
- создать нового пользователя;
- изменить информацию о пользователе;
- посмотреть все банковские счета всех пользователей;
- отфильтровать по поиску нужные счета;
- открыть/закрыть счет;
- сделать перевод со счета на другой счет (с автоматическими проверками на достаточность суммы для перевода, наличии счетов у получателя, заполненности всех необходимых полей);
- просматривать все транзакции и фильтровать по сумме, дате, фио отправителя/получателя или номеру счета.

Админ может:
- создать новый тип банковского продукта;
- откатить операции;
- удалить карточки клиентов, счета (с автоматическими проверками - например, нельзя удалить карточку клиента, если у него есть активные счета).

Клиент может:
- посмотреть выписку по счетам;
- посмотреть все свои счета;
- отправить перевод пользователю или себе на другой счет;
- закрыть свой счет;
- изменить свои данные в карточке (например, адрес);
- изменить пароль в личный кабинет.

Автоматическая отправка отчетов руководителю об открытых счетах и новых клиентов за прошедшую неделю.
