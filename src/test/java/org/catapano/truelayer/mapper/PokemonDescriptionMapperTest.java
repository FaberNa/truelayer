package org.catapano.truelayer.mapper;

import org.catapano.truelayer.dto.PokeApiResponse;
import org.catapano.truelayer.exception.PokemonDescriptionNotFoundException;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PokemonDescriptionMapperTest {

    private final PokemonDescriptionMapper mapper = new PokemonDescriptionMapper();

    @Test
    void shouldExtractDescriptionWhenPresent() {
        PokeApiResponse response = pokeApiResponseWithDescription("Some description");

        String description = mapper.extractDescription(response, "pikachu");

        assertThat(description).isEqualTo("Some description");
    }

    @Test
    void shouldThrowPokemonNotFoundExceptionWhenPokemonDetailIsEmpty() {

        PokeApiResponse response = new PokeApiResponse(
                new PokeApiResponse.Data(List.of())
        );

        assertThatThrownBy(() -> mapper.extractDescription(response, "unknown"))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessage("Pokemon not found: unknown");
    }

    @Test
    void shouldThrowPokemonNotFoundExceptionWhenDescriptionIsMissing() {
        // given
        String pokemonName = "pikachu";
        PokeApiResponse response = new PokeApiResponse(
                new PokeApiResponse.Data(List.of(
                        new PokeApiResponse.PokemonDetail(
                                pokemonName,
                                false,
                                null,
                                List.of()
                        )
                ))
        );


        assertThatThrownBy(() -> mapper.extractDescription(response, pokemonName))
                .isInstanceOf(PokemonDescriptionNotFoundException.class)
                .hasMessage("Description not found for pokemon: " + pokemonName);
    }


    private PokeApiResponse pokeApiResponseWithDescription(String description) {
        return new PokeApiResponse(
                new PokeApiResponse.Data(List.of(
                        new PokeApiResponse.PokemonDetail(
                                "pikachu",
                                false,
                                null,
                                List.of(new PokeApiResponse.FlavorText(description))
                        )
                ))
        );
    }
}