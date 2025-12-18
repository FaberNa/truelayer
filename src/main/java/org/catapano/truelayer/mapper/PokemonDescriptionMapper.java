package org.catapano.truelayer.mapper;

import org.catapano.truelayer.dto.PokeApiResponse;
import org.catapano.truelayer.exception.PokemonDescriptionNotFoundException;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PokemonDescriptionMapper {

    /**
     * Extracts the description of a Pokémon from the PokeApiResponse.
     * @param response
     * @param name
     * @return
     */
    public String extractDescription(PokeApiResponse response,String name) {
        var details = response.data() != null ? response.data().pokemon_detail() : null;

        if (details == null || details.isEmpty()) {
            throw new PokemonNotFoundException(name);
        }

        return details.stream()
                .findFirst()
                .map(PokeApiResponse.PokemonDetail::description)
                .filter(list -> list != null && !list.isEmpty())
                .map(list -> list.getFirst().flavor_text())
                .filter(s -> s != null && !s.isBlank())
                .orElseThrow(() -> new PokemonDescriptionNotFoundException(name));
    }

    public String extractHabitat(PokeApiResponse response) {
        return response.data().pokemon_detail().stream()
                .findFirst()
                .map(PokeApiResponse.PokemonDetail::pokemonhabitat)
                .map(PokeApiResponse.PokemonHabitat::name)
                .orElse(null);
    }

    public boolean extractLegendary(PokeApiResponse response) {
        return response.data().pokemon_detail().stream()
                .findFirst()
                .map(PokeApiResponse.PokemonDetail::is_legendary)
                .orElse(false);
    }
}
