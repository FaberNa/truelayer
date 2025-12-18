package org.catapano.truelayer.client;

import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.dto.GraphQLRequest;
import org.catapano.truelayer.dto.PokeApiResponse;
import org.catapano.truelayer.mapper.PokemonDescriptionMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Map;

@Component
public class PokemonClient {

    private final WebClient webClient;
    private final PokemonDescriptionMapper pokemonDescriptionMapper;

    private static final String POKEMON_SPECIES_QUERY = """
            query samplePokeAPIquery($name: String!) {
              pokemon_detail: pokemonspecies(where: {name: {_eq: $name}}) {
                name
                is_legendary
                pokemonhabitat { name }
                description: pokemonspeciesflavortexts(
                  where: {
                  }
                  limit: 1
                ) { flavor_text }
              }
            }
            """;

    public PokemonClient(WebClient pokemonInfoWebClient, PokemonDescriptionMapper pokemonDescriptionMapper) {
        this.webClient = pokemonInfoWebClient;
        this.pokemonDescriptionMapper = pokemonDescriptionMapper;
    }

    public Pokemon getPokemonInfo(String name) throws WebClientException {

        String query = POKEMON_SPECIES_QUERY.formatted(name);

        GraphQLRequest body = new GraphQLRequest(query, Map.of("name", name));

        PokeApiResponse response = webClient.post()
                .uri("/v1beta2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(PokeApiResponse.class)
                .block();
        return mapPokeApiResponseToPokemon(response, name);
    }

    protected Pokemon mapPokeApiResponseToPokemon(PokeApiResponse pokeApiResponse, String name) {
        return Pokemon.builder().description(pokemonDescriptionMapper.extractDescription(pokeApiResponse, name))
                .habitat(pokemonDescriptionMapper.extractHabitat(pokeApiResponse))
                .name(name)
                .isLegendary(pokemonDescriptionMapper.extractLegendary(pokeApiResponse))
                .build();
    }


}
