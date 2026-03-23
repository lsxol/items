REST API do zarządzania notatkami. 

* Język: Java 21 
* Framework: Spring boot 3 z zabezpieczeniami Spring Security + JWT
* Baza danych: MySQL 
* Migracje: Liquibase
* Audyt: Hibernate Envers
* Rate Limiting: Bucket4j
* Testy: Mockito, Testcontainers
* Konteneryzacja: Docker

Uruchomienie aplikacji wymaga jedynie użycia "docker-compose up -d" w terminalu folderu z aplikacją.
Swagger dostępny jest pod portem 8000.

W aplikacji wykorzystałem architekturę heksagonalną, trochę o niej już czytałem i bawiłem się z nią wcześniej, więc chciałem 
utrwalić sobie wiedzę. Na wymagania projektowe to za dużo, ale jeśli miałem spędzać czas na budowaniu tej aplikacji to chciałem 
coś z niej przy okazji wyciągnąć.
Wyraźny podział warstw domenowej, aplikacyjnej oraz infrastrukturalnej sprawia, że pisanie testów jest banalne, 
kod jest prostszy w obsłudze na utrzymaniu i czystszy, a sama aplikacja znacznie lepiej dostosowana do dalszego rozwoju.

