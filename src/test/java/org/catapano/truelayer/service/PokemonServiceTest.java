package org.catapano.truelayer.service;

import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.exception.PokemonDescriptionNotFoundException;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.catapano.truelayer.helper.AbstractWireMockIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PokemonServiceTest extends AbstractWireMockIT {

    @Autowired
    PokemonService service;


    @Test
    void shouldReturnPokemonInfoWhenPokemonExists() {
        String flavorText = "It was created by\na scientist after\nyears of horrific\fgene splicing and\nDNA engineering\nexperiments.";
        String pokemonName = "mewtwo";
        String habitat = "rare";
        boolean isLegendary = true;

        stubsPokemonApi().stubPokemonSpeciesOk(
                pokemonName,
                isLegendary,
                habitat,
                flavorText
        );

        var result = service.getPokemonInfo(pokemonName);
        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly(pokemonName, habitat, isLegendary, flavorText);
    }

    @Test
    void shouldThrowPokemonNotFoundExceptionWhenPokemonDoesNotExist() {
        String pokemonName = "unknownpokemon";

        stubsPokemonApi().stubPokemonSpeciesEmptyResult(pokemonName);

        assertThatThrownBy(() -> service.getPokemonInfo(pokemonName))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessageContaining("Pokemon not found: " + pokemonName);
    }

    @Test
    @DisplayName("Throws PokemonNotFoundException when trying to search a Pokémon that does not exist")
    void pokemonDescriptionNotFound(){
        String pokemonName = "withoudescription";
        stubsPokemonApi().stubPokemonSpeciesWithoutDescription(
                pokemonName,
                false,
                "forest"
        );

        assertThatThrownBy(() -> service.getPokemonInfo(pokemonName))
                .isInstanceOf(PokemonDescriptionNotFoundException.class)
                .hasMessage("Description not found for pokemon: " + pokemonName);
    }

}