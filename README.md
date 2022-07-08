## Sobre el proyecto
API Rest que brinda cierta información básica y especificada de los Pokemons. Para esto la app consume la API de [pokeApi][1]

**Construído con:**
- Java 17
- Spring Boot 2.7.1

**Desplegado en:**
- Amazon Web Services EC2
- Plataforma de instancia de servidor virtual: Ubuntu/Linux

**Testeado con:**
- Swagger 3.0.0 (/swagger-ui/index.html)

------------

**Dependencias:**
1. Spring Boot Starter Web
2. Spring Boot Devtools
3. Project Lombok
4. Spring Fox 3.0.0
5. Json 20220320

**Más información**
- Herramienta de gestión de proyectos: Maven
- Packaging: Jar
- Endpoints: "/pokemons", "pokemons/{id}"
- El endpoint "/pokemons/{id}" para solicitar información de un solo pokemon acepta valores alfanuméricos (tanto enteros como strings). Sea cual fuere el valor ingresado, la API responderá si lo encuentra o no.
- El primer endpoint "/pokemons" trabaja sobre la URL oficial de pokemon "https://pokeapi.co/api/v2/pokemon/" que tiene un límite de 20 pokemones. Pero para las consultas por un pokemon en particular ("pokemons/{id}") no tiene límite.


[1]: https://pokeapi.co/api/v2/pokemon "pokeapi.co"