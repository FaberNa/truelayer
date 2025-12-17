package org.catapano.truelayer.service;

import org.catapano.truelayer.client.PokemonClient;
import org.catapano.truelayer.client.PokemonTranslationClient;
import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    PokemonClient pokemonClient;
    @Mock
    PokemonTranslationClient translationClient;

    @InjectMocks
    PokemonService service;

    @Test
    void shouldReturnPokemonInfoWhenPokemonExists() {
        Pokemon pikachu = new Pokemon(
                "mewtwo",
                "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
                "rare",
                false
        );

        var result = service.getPokemonInfo("mewtwo");
        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly("mewtwo", "rare", true,"It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.");
    }

    @Test
    void shouldThrowExceptionNotFoundWhenPokemonNotExists() {

        assertThatThrownBy(() -> service.getPokemonInfo("mew"))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessageContaining("Pokemon not exist");
    }

    @Test
    void shouldReturnPokemonWithTranslatedDescription() {
        Pokemon mewtwo = new Pokemon(
                "mewtwo",
                "Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.",
                "rare",
                false
        );

        var result = service.getPokemonTranslation("mewtwo");

        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly("mewtwo", "rare", true,"Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.");


    }

    @Test
    @DisplayName("Throws PokemonNotFoundException when trying to translate a Pokémon that does not exist")
    void pokemonTranslationNotFound() {

        assertThatThrownBy(() -> service.getPokemonTranslation("mew"))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessageContaining("Pokemon not exist");
    }


}
