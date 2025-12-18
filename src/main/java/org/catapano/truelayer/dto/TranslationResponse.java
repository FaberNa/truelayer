package org.catapano.truelayer.dto;


public record TranslationResponse(
        Contents contents
) {
    public record Contents(
            String translated,
            String text,
            String translation
    ) {}
}
