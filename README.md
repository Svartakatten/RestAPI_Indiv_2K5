# Bibliotek REST API: Prestanda & Cachning

Detta projekt är ett fullskaligt REST API byggt med Spring Boot 4. Huvudsyftet med projektet är att demonstrera hur en distribuerad cachningsnivå (Redis) skyddar webbserverns trådpool (Tomcat) och drastiskt minimerar latens under tunga trafikspikar.

## För att komma igång

### Förkunskaper
För att köra detta projekt lokalt behöver du följande installerat på din maskin:
* **Java 17 / 21** (eller nyare)
* **Docker** (för att köra Redis-cachen och HashiCorp Vault)
* **Git** ### Starta applikationen

### Starta Docker-containern

För att köra denna applikation lokalt via Docker Compose behöver du definiera miljövariabler.

skapa din egen miljöfil:
med namn ".env"

Öppna .env och lägg till DOCKERHUB_USERNAME=svartakatten eller eget om du kör det själv.

Starta systemet:
docker compose up -d


### API-Dokumentation (Swagger UI)

När servern är igång genereras en interaktiv OpenAPI-dokumentation automatiskt. Här kan du testa alla endpoints direkt i webbläsaren.

URL: http://localhost:8080/swagger-ui/index.html

Autentisering: API:et är skyddat av Spring Security (Basic Auth). Klicka på knappen Authorize och logga in med:

Användarnamn: admin

Lösenord: admin-secret-disabled

### Prestandatestning och Arkitekturbevis

För att mäta och bevisa effektiviteten av Redis-cachen har API:et stresstestats med Apache JMeter. Testet simulerade en överbelastningsattack med 200 samtidiga användare som skickade totalt 10 000 HTTP-förfrågningar.

Testet jämförde två specifika endpoints:

GET /api/v2/books/{id} (Skyddad av Redis-cache)

GET /api/v2/books/nocache/{id} (Bypass på cachen, simulerar ett tungt databasanrop mot H2)

### Testloggar & Bevis
Alla resultat, inklusive rådata och visuella bevis, finns sparade i mappen /TestLogs i detta repository:

summary.csv - Den fullständiga loggfilen med rådata från JMeter.

Summary_Screenshot.png - En skärmdump av resultatsammanställningen.

### Slutsats av Testet
Utan cachning (direkt mot databasen) resulterade den höga trafiken i att Tomcats trådpool överbelastades, vilket skapade en massiv kö och en genomsnittlig svarstid på ~15 500 ms.

Genom att låta Redis avlasta databasen hanterades exakt samma trafikmängd på i genomsnitt ~43 ms. Cachen förhindrade total systemkollaps och reducerade svarstiderna med över 99,7 %, helt utan felkoder.