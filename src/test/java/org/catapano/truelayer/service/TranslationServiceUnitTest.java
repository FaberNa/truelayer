package org.catapano.truelayer.service;

import org.catapano.truelayer.domain.Pokemon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslationServiceUnitTest {
    private final TranslationService service = new TranslationService(null);

    @Test
    void shouldReturnYodaWhenLegendary() {
        Pokemon p = Pokemon.builder().isLegendary(true).habitat("rare").build();
        assertEquals(TranslationStyle.YODA, service.chooseStyle(p));
    }

    @Test
    void shouldReturnYodaWhenHabitatIsCave_caseInsensitive() {
        Pokemon p = Pokemon.builder().isLegendary(false).habitat("CAVE").build();
        assertEquals(TranslationStyle.YODA, service.chooseStyle(p));
    }


}