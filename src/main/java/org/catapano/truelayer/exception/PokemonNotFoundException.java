package org.catapano.truelayer.exception;

public class PokemonNotFoundException extends RuntimeException {

    private final String pokemonName;

    public PokemonNotFoundException(String pokemonName) {
        super("Pokemon not found: " + pokemonName);
        this.pokemonName = pokemonName;
    }

    public String getPokemonName() {
        return pokemonName;
    }
}