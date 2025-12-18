package org.catapano.truelayer.dto;

import java.util.List;

public record PokeApiResponse(Data data) {

    public record Data(
            List<PokemonDetail> pokemon_detail
    ) {}

    public record PokemonDetail(
            String name,
            boolean is_legendary,
            PokemonHabitat pokemonhabitat,
            List<FlavorText> description
    ) {}

    public record PokemonHabitat(String name) {}

    public record FlavorText(String flavor_text) {}
}