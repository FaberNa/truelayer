package org.catapano.truelayer.service;

import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.catapano.truelayer.helper.AbstractWireMockIT;
import org.catapano.truelayer.service.TranslationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TranslationServiceTest extends AbstractWireMockIT {

    @Autowired
    TranslationService service;

    @Test
    @DisplayName("Should return Pokémon with Shakespeare translated description when Pokémon's habitat is not cave and is not legendary")
    void shouldReturnShakespeareTranslateResponseWhenPokemonUnsatisfiedReq(){
        String descriptionTranslateShakespeare="At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms.";
        String pokemonName = "pikachu";
        String description = "When several of\nthese POKéMON\ngather, their\felectricity could\nbuild and cause\nlightning storms.";
        boolean isLegendary = false;
        String habitat = "forest";
        Pokemon pokemonInput=generatePokemon(pokemonName,isLegendary,habitat,description);

        stubsPokemonApi().stubPokemonSpeciesOk(
                pokemonName,
                isLegendary,
                habitat,
                description
        );

        stubsTranslationApi().stubTranslateOk(
                descriptionTranslateShakespeare,
                description,
                "shakespeare"
        );

        Pokemon result=service.getPokemonTranslated(pokemonInput);

        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly(pokemonName, habitat, isLegendary,descriptionTranslateShakespeare);
    }

    private Pokemon generatePokemon(String pokemonName, boolean isLegendary, String habitat, String description) {
        return   Pokemon.builder().name(pokemonName)
                .isLegendary(isLegendary)
                .habitat(habitat)
                .description(description)
                .build();
    }


    @Test
    @DisplayName("Should return Pokémon with Yoda translated description when Pokémon's habitat is cave")
    void shouldReturnPokemonWithYodaTranslatedDescriptionCave() {
        String originalDescription = "When spotted, this\nPOKéMON escapes\nbackward by furi\fously boring into\nthe ground with\nits tail.";
        String habitat = "cave";
        boolean isLegendary = false;
        String pokemonName = "dunsparce";
        Pokemon pokemonInput=generatePokemon(pokemonName,isLegendary,habitat,originalDescription);


        stubsPokemonApi().stubPokemonSpeciesOk(
                pokemonName,
                isLegendary,
                habitat,
                originalDescription
        );
        String translatedDescription = "When spotted,This pokémon escapes backward by furiously boring into the ground with its tail.";
        stubsTranslationApi().stubTranslateOk(
                translatedDescription,
                originalDescription,
                "yoda"
        );

        var result = service.getPokemonTranslated(pokemonInput);

        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly(pokemonName, habitat, isLegendary, translatedDescription);
    }

    @Test
    @DisplayName("Should return Pokémon with Yoda translated description when Pokémon is legendary")
    void shouldReturnPokemonWithYodaTranslatedDescription() {
        String originalDescription = "It was created by\na scientist after\nyears of horrific\fgene splicing and\nDNA engineering\nexperiments.";
        String habitat = "rare";
        boolean isLegendary = true;
        String pokemonName = "mewtwo";
        Pokemon pokemonInput=generatePokemon(pokemonName,isLegendary,habitat,originalDescription);

        stubsPokemonApi().stubPokemonSpeciesOk(
                pokemonName,
                isLegendary,
                habitat,
                originalDescription
        );
        String translatedDescription = "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.";

        stubsTranslationApi().stubTranslateOk(
                translatedDescription,
                originalDescription,
                "yoda"
        );



        var result = service.getPokemonTranslated(pokemonInput);

        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly(pokemonName, habitat, isLegendary, translatedDescription);
    }

    @Test
    void shouldReturnPokemonWithOriginalDescriptionWhenTranslatedAPIError() {
        String originalDescription = "It was created by\na scientist after\nyears of horrific\fgene splicing and\nDNA engineering\nexperiments.";
        String habitat = "rare";
        boolean isLegendary = true;
        String pokemonName = "mewtwo";
        Pokemon pokemonInput=generatePokemon(pokemonName,isLegendary,habitat,originalDescription);
        stubsPokemonApi().stubPokemonSpeciesOk(
                pokemonName,
                isLegendary,
                habitat,
                originalDescription
        );
        stubsTranslationApi().stubRateLimit429("yoda");

        var result = service.getPokemonTranslated(pokemonInput);

        assertThat(result)
                .extracting(
                        Pokemon::getName,
                        Pokemon::getHabitat,
                        Pokemon::getIsLegendary,
                        Pokemon::getDescription
                )
                .containsExactly(pokemonName, habitat, isLegendary, originalDescription);
    }

    @Test
    @DisplayName("Throws PokemonNotFoundException when trying to translate a Pokémon that has empty description")
    void pokemonDescriptionEmpty() {
        String pokemonName = "unknown";

        Pokemon pokemonInput=generatePokemon(pokemonName,false,"forest","");

        Pokemon result = service.getPokemonTranslated(pokemonInput);

        assertThat(result.getDescription()).isEmpty();

    }

    @Test
    @DisplayName("Throws PokemonNotFoundException when trying to translate a Pokémon that does not exist")
    void pokemonDescriptionNull() {
        String pokemonName = "unknown";
        Pokemon pokemonInput=generatePokemon(pokemonName,false,"forest",null);
        Pokemon result = service.getPokemonTranslated(pokemonInput);
        assertThat(result.getDescription()).isNull();
    }

}