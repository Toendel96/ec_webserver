# Markdown syntax guide
https://www.markdownguide.org/basic-syntax/

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
