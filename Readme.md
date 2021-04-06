# Markdown syntax guide
https://www.markdownguide.org/basic-syntax/

# Team TODOs

    // APPLICATION ???
    (kanskje til oppsummering)

    // USER:
    (NB: To ulike controllere?) user-controller / admin-controller)
    1.1 Logge inn / Autentisere administrator (inn: brukernavn og passord) > token
    1.2 Validere token (inn: token) > boolean
    *1.3* Liste opp alle brukere

    // ENVIRONMENT:
    *2.1* Liste opp alle miljøer
    2.2 Hente konfigurasjoner for et bestemt miljø (inn: environment_id) > liste over konfigurasjoner
    2.3 Hente oppsummering
    2.4 Endre beskrivelse for et eksisterende miljø (kun admin)
    2.5 Legge til nytt miljø (kun admin)
    2.6

    // CONFIGURATION:
    *3.1* Hente konfigurasjoner (inn: environment_id) > liste over konfigurasjoner
    3.2 Legge til configurasjon
    3.3 Endre konfigurasjon
    3.4 Slette konfigurasjon
    3.5 Filtrere konfigurasjoner (ref tid for endring)

# Useful project links

## GitHub Code Repositories
Web client: https://github.com/ZiggyHowland/ec_webclient \
Web server: https://github.com/Toendel96/ec_webserver

## Swagger API documentation
(Requires backend server to be running) \
http://localhost:8111/swagger-ui/ 


# Documentation

## Swagger Documentation
The API (REST controllers) are available through a swagger dependency.  

Swagger documentation added by:
1. Adding the *springfox-boot-starter* dependency in pom.xml
2. Adding *public Docket api()* - function in root **Application.java**-file
    1. Updated imports in **Application.java** to match api-function.

NB: Remember to update the Maven-dependencies from the POM-file.
   
