package org.catapano.truelayer.service;

import org.catapano.truelayer.client.PokemonClient;
import org.catapano.truelayer.domain.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class PokemonService {

    private final PokemonClient pokemonClient;

    PokemonService(PokemonClient pokemonClient) {
        this.pokemonClient = pokemonClient;
    }

    /**
     * Get Pokemon info by name, with caching.
     * @param name
     * @return
     * @throws WebClientException
     */
    @Cacheable(cacheNames = "pokemonInfo", key = "#name")
    public Pokemon getPokemonInfo(String name) throws WebClientException {
        return pokemonClient.getPokemonInfo(name);
    }

}