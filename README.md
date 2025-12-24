## Problem Analysis & Domain Study

I have started by analyzing the problem domain, focusing in particular on the data model and capabilities exposed by external APIs.

The first step was to study the **PokéAPI** domain, understanding how Pokémon-related data is structured and accessed, including:

- Pokémon species information
- Habitats
- Legendary status
- Pokédex flavor texts and localization aspects

During this initial exploration, I quickly noticed that, in addition to the classic REST endpoints, the Pokédex data is also accessible through **GraphQL endpoints**, which provide more flexibility when querying related resources and reduce over-fetching by allowing more precise data selection.

Reference:

- https://pokeapi.co/
- https://pokeapi.co/docs/graphql

While exploring the Pokédex APIs through the GraphQL console at https://graphql.pokeapi.co/v1beta2/console, I noticed that the Pokémon description is exposed via the pokemonspeciesflavortexts field.
For the purpose of this exercise, I assumed that taking the first available  flavor text was sufficient.
Similarly, the habitat information is provided through the pokemonHabitat field.
Based on these observations, I built the corresponding GraphQL query to retrieve all the required data in a single request.

In parallel, I explored the **Fun Translations API**, evaluating how textual transformations can be applied to existing descriptions.
Specifically, I analyzed the following translation services:

- Yoda-style translation
  https://api.funtranslations.com/translate/yoda.json
- Shakespeare-style translation
  https://api.funtranslations.com/translate/shakespeare.json

I started by defining the skeleton of the problem:

- domain class
- service layer
- external clients

Only after that, I moved on to writing the tests.
Perhaps using a separate DTO layer is unnecessary since the internal API contract maps 1:1 to the domain model.
I decide to separate the service one for get the pokemon info other one for get the pokemon translation in this way each service has own responsibility

## External client

A dedicated WebClientConfig class is used to centralize WebClient configuration (base URLs, timeouts, and headers) and promote consistency and reusability across all external API clients.
After implementing the two clients—one for the translation API and one for the Pokémon API—I modeled their response objects starting from real API responses, using tooling support where helpful.

## Caching Strategy for External APIs

I considered that, given the rate limits defined in their contracts, introducing caching could be a useful optimization.
The idea is to use two separate caches: one for translation results and one for standard Pokémon data.
Failed or rate-limited translation attempts are deliberately excluded from caching, ensuring that temporary errors do not permanently affect future requests
and allowing the system to recover automatically once the external service becomes available again

## API Response, modeling Exception and logging

I introduced the PokemonNotFoundException to  handle the case where the pokemon is not found, since the Pokémon API return an empty result set for certain queries.
I also add the PokemonDescriptionNotFoundException to handle the case where the description is not found for a given Pokémon. During a test with this query i notice that description is empty
For modeling an error i decide to not use custom DTO but use directly ProblemDetail provided by spring framework since is enough for this case
```json
query samplePokeAPIquery {
  pokemon_detail: pokemonspecies(where: {name: {_eq: "dunsparce"}}) {
    name
    is_legendary
    pokemonhabitat {
      name
    }
    description: pokemonspeciesflavortexts(
      where: {
        language: {name: {_eq: "en"}}
        version: {name: {_eq: "red"}}
      }
      limit: 10
    ) {
      flavor_text
    }
  }
} 
```
Error handling for the translation API is encapsulated in the HTTP client, which returns an Optional.empty() in case of  failures, allowing the service layer to  fall back to the original description.

## Service Layer Design

The service layer is composed of three services: PokemonService, TranslationService, PokemonTranslationService.
- PokemonService: Responsible for fetching Pokémon data from the PokéAPI.
- TranslationService: Handles text translation using the Fun Translations API.
- PokemonTranslationService: Orchestrates the process of retrieving Pokémon data and applying translations when necessary.
In this way each service has a single responsibility and can be tested independently. 
Decoupling the services also allows for easier maintenance and potential future enhancements, such as adding new translation styles or integrating additional data sources.

## HOW TO RUN THE APPLICATION

The application is built using Spring Boot and can be run locally with the following steps:

Make sure the following are installed on your system:
•	Java 24
•	Maven 3.9+

Verify the installed versions:
```bash
java -version
mvn -version
```

From the project root directory, run:
```bash 
mvn spring-boot:run
```

The application will start on http://localhost:8082/swagger-ui.html by default.
Alternatively, you can build and run the executable JAR:
```bash
mvn clean package
java -jar target/truelayer0.0.1-SNAPSHOT.jar
```

Default configuration
•	Default port: 8082
Configuration file:
•	 src/main/resources/application.properties

### What I’d do differently for production
For a production deployment, I would consider the following enhancements:

• Add  retries with backoff + jitter for both external APIs (PokéAPI + FunTranslations).
• Add a circuit breaker / bulkhead  so FunTranslations failures can’t cascade and overload your app.
• Add rate limiting  client-side throttling for FunTranslations (since it’s explicitly rate-limited and you already considered caching partly for this reason)
• Use a distributed cache  (e.g., Redis) instead of in-memory caching to support scalability and persistence across application restarts.
• Implement monitoring and alerting  to track application health, performance metrics ( like rate error ) 
• Adding security measures  such as API authentication, HTTPS, and input validation to protect against common vulnerabilities.
• Add secret scanning easily, no keys in repo


## Bonus track 
From the root of the project, build the executable JAR:
```bash
mvn clean package
```
This step is required to generate the JAR file under the target/ directory, which is used by the Docker image.
Before proceeding with the Docker steps, make sure Docker is installed and running locally on your machine.
To build the Docker image, run from the root of the project:
```bash
docker build -f docker/Dockerfile -t pokedex:last .
```
This command builds the Docker image using the specified Dockerfile and tags it as pokedex:last.
To run the Docker container, execute the following command (you can choose any external port you prefer, e.g. 8087):
```bash
docker run -p 8087:8082 pokedex:last
```
The application will be available at: http://localhost:8082/swagger-ui.html

For this exercise I intentionally kept the Docker image simple and predictable (copy the built JAR and run it).
CDS/AppCDS optimizations were intentionally not enabled to keep the Docker setup minimal and portable for the exercise. ( some example in Dockerfile.prod )
