# Markdown syntax guide
https://www.markdownguide.org/basic-syntax/

# Team TODOs
### Samarbeid
- [ ] Avtale møtestruktur (faste tidspunkt, eller alltid på)?
- [ ] Fremdriftsplan og ansvarsfordeling

### Applikasjon
General/Application:
- [ ] Hente oppsummering

User:
- [ ] **Liste opp alle brukere**
- [ ] Logge inn / Autentisere administrator (inn: brukernavn og passord) > token
- [ ] Validere token (inn: token) > boolean
NB: To ulike controllere? user-controller vs. admin-controller)
     

ENVIRONMENT:

- [ ] **Liste opp alle miljøer**
- [ ] Hente konfigurasjoner for et bestemt miljø (inn: environment_id) > liste over konfigurasjoner
- [ ] *Endre beskrivelse for et eksisterende miljø (kun admin)*
- [ ] *Legge til nytt miljø (kun admin)*

CONFIGURATION:
- [ ] **Hente konfigurasjoner (inn: environment_id) > liste over konfigurasjoner**
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
   
