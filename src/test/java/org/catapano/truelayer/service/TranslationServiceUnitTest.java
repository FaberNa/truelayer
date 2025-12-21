package org.catapano.truelayer.service;

import org.catapano.truelayer.domain.Pokemon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslationServiceUnitTest {
    private final TranslationService service = new TranslationService(null);

    @Test
    void shouldReturnYodaWhenLegendary() {
        Pokemon p = new Pokemon("","rare","",true);
        assertEquals(TranslationStyle.YODA, service.chooseStyle(p));
    }

    @Test
    void shouldReturnYodaWhenHabitatIsCave_caseInsensitive() {
        Pokemon p = new Pokemon("","Cave","",true);
        assertEquals(TranslationStyle.YODA, service.chooseStyle(p));
    }


}