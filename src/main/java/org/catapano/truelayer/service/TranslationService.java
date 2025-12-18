package org.catapano.truelayer.service;

import org.catapano.truelayer.client.TranslationClient;
import org.catapano.truelayer.domain.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TranslationService {
    private final TranslationClient translationClient;

    public TranslationService(TranslationClient translationClient) {
        this.translationClient = translationClient;
    }

    public Pokemon getPokemonTranslated(Pokemon pokemon){

        TranslationStyle translationStyle = chooseStyle(pokemon);

        translateDescription(pokemon.getDescription(),translationStyle)
                .ifPresent(pokemon::setDescription);

        return pokemon;
    }

    TranslationStyle chooseStyle(Pokemon pokemon) {
        return (Boolean.TRUE.equals(pokemon.getIsLegendary())
                || "cave".equalsIgnoreCase(pokemon.getHabitat()))
                ? TranslationStyle.YODA
                : TranslationStyle.SHAKESPEARE;
    }

    @Cacheable(
            cacheNames = "translatedPokemons",
            key = "#style.name() + ':' + T(java.util.Objects).hash(#description)",
            unless = "#result == null || #result.isEmpty()"
    )
     public Optional<String> translateDescription(String description, TranslationStyle style) {
        if (description == null || description.isBlank()) return Optional.empty();

        return switch (style) {
            case YODA -> translationClient.getTranslationToYoda(description);
            case SHAKESPEARE -> translationClient.getTranslationToShakespeare(description);
        };
    }

}