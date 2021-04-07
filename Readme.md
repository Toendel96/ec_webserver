# Markdown syntax guide
https://www.markdownguide.org/basic-syntax/

# Team TODOs
### Samarbeid
- [X] Avtale møtestruktur (faste tidspunkt, eller alltid på)?
- [ ] Fremdriftsplan og ansvarsfordeling


### Applikasjon
General/Application:
- [ ] Hente oppsummering

USER:
- [X] SALIM **Liste opp alle brukere**
- [ ] SIGBJØRN Logge inn / Autentisere administrator (inn: brukernavn og passord) > token
- [X] SIGBJØRN Validere token (inn: token) > boolean
NB: To ulike controllere? user-controller vs. admin-controller)
     

ENVIRONMENT:
- [X] PETTER **Liste opp alle miljøer**
- [X] PETTER Hente konfigurasjoner for et bestemt miljø (inn: environment_id) > liste over konfigurasjoner
- [X] PETTER *Endre beskrivelse for et eksisterende miljø (kun admin)*
- [ ] PETTER *Legge til nytt miljø (kun admin)*

CONFIGURATION:
- [X] SIGBJØRN **Hente konfigurasjoner (inn: environment_id) > liste over konfigurasjoner**
- [ ] *Legge til configurasjon (kun admin)*
- [ ] *Endre konfigurasjon (kun admin)*
- [ ] *Slette konfigurasjon (kun admin)*
- [ ] Filtrere konfigurasjoner (ref tid for endring)


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
   

# Security

## Token-based API authentication
Source: https://blog.softtek.com/en/token-based-api-authentication-with-spring-and-jwt 
