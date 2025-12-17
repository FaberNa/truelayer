package org.catapano.truelayer.service;

import org.catapano.truelayer.client.PokemonClient;
import org.catapano.truelayer.client.PokemonTranslationClient;
import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.dto.PokemonResult;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokemonClient pokemonClient;
    private final PokemonTranslationClient translationClient;

    PokemonService(PokemonClient pokemonClient, PokemonTranslationClient translationClient) {
        this.pokemonClient = pokemonClient;
        this.translationClient = translationClient;
    }

    public Pokemon getPokemonInfo(String name){
        return Pokemon.builder().build();
    }


    public Pokemon getPokemonTranslation(String name){
        return  Pokemon.builder().build();
    }
}
