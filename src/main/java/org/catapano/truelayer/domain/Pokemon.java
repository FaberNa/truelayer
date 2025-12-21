package org.catapano.truelayer.domain;

public record Pokemon(
        String name,
        String habitat,
        String description,
        Boolean isLegendary
) {}
