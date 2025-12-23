package org.catapano.truelayer.exception;

public class PokemonNameNotFoundException extends RuntimeException {


    public PokemonNameNotFoundException() {
        super("Pokemon name not found in pokeApi service");
    }


}