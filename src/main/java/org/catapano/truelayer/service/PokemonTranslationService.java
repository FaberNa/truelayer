package org.catapano.truelayer.service;

import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PokemonTranslationService {

    private final PokemonService pokemonService;
    private final TranslationService translationService;

    public PokemonTranslationService(PokemonService pokemonService, TranslationService translationService) {
        this.pokemonService = pokemonService;
        this.translationService = translationService;
    }

    public Pokemon getPokemonWithTranslatedDescription(String name) {
        Pokemon pokemon = pokemonService.getPokemonInfo(name);
        return Optional.ofNullable(pokemon)
                .map(translationService::getPokemonTranslated)
                .orElseThrow(() -> new PokemonNotFoundException(name));
    }

}
