# FootballScoutAI: Backend

API REST realizat cu **Java** și **Spring Boot**, care gestionează ligi, echipe și jucători și oferă funcționalitatea de scouting asistat de AI. Datele sunt stocate într-o bază de date **MySQL**.

## Structura proiectului

Proiectul Maven se află în `demo/demo/`:

```
demo/demo/
├── src/main/java/ProiectSCD_Dorel/
│   ├── DemoApplication.java
│   ├── config/
│   │   └── WebConfig.java          # configurare web (CORS etc.)
│   ├── controller/
│   │   ├── AIScoutController.java
│   │   ├── LeagueController.java
│   │   ├── PlayerController.java
│   │   └── TeamController.java
│   ├── model/                      # entități: League, Player, Team
│   ├── repo/                       # acces la date (leagueRepo, playerRepo, teamRepo)
│   └── service/                    # logica de business
├── src/main/resources/
│   └── application.properties
├── uploads/                        # imagini încărcate (ex: poze jucători)
└── pom.xml
```

Arhitectura este pe straturi: **Controller → Service → Repository → Bază de date**.

## Cerințe

- JDK 17 sau mai nou (verifică versiunea din `pom.xml`)
- MySQL Server
- Maven (sau wrapper-ul inclus: `mvnw` / `mvnw.cmd`)

## Configurare

1. Creează o bază de date în MySQL, de exemplu:

   ```sql
   CREATE DATABASE football_scout;
   ```

2. Editează `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/football_scout
   spring.datasource.username=UTILIZATOR
   spring.datasource.password=PAROLA
   spring.jpa.hibernate.ddl-auto=update
   ```

> **Atenție:** nu urca în repository parole sau chei API. Folosește variabile de mediu sau un fișier `application.properties` exclus prin `.gitignore`.

## Rulare

Din folderul `demo/demo`:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Serverul pornește implicit la `http://localhost:8080`.

## Resurse API

| Resursă | Controller |
|---|---|
| Ligi | `LeagueController` |
| Echipe | `TeamController` |
| Jucători | `PlayerController` |
| Scouting AI | `AIScoutController` |

Rutele exacte se găsesc în clasele din pachetul `controller`.

## Teste

```bash
.\mvnw.cmd test
```

## Tehnologii

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
