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

In parallel, I explored the **Fun Translations API**, evaluating how textual transformations can be applied to existing descriptions.
Specifically, I analyzed the following translation services:
- Yoda-style translation  
  https://api.funtranslations.com/translate/yoda.json
- Shakespeare-style translation  
  https://api.funtranslations.com/translate/shakespeare.json
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

In parallel, I explored the **Fun Translations API**, evaluating how textual transformations can be applied to existing descriptions.
Specifically, I analyzed the following translation services:
- Yoda-style translation  
  https://api.funtranslations.com/translate/yoda.json
- Shakespeare-style translation  
  https://api.funtranslations.com/translate/shakespeare.json

This initial analysis allowed me to better understand the data relationships, constraints, and integration points between the Pokémon domain and external text-translation services, laying the groundwork for a clean and extensible solution design.

For model better i prefer use Lombok for not generate useless code
I started by defining the skeleton of the problem: 
- domain class
- service layer
- external clients 
Only after that, I moved on to writing the tests. Perhaps using separate DTO layer is unecessary since the API contact maps 1:1 to the domain model 
