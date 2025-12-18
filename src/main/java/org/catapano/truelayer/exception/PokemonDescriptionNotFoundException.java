package org.catapano.truelayer.exception;

public class PokemonDescriptionNotFoundException extends RuntimeException {
    public PokemonDescriptionNotFoundException(String name) {
        super("Description not found for pokemon: " + name);
    }
}