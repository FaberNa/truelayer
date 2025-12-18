package org.catapano.truelayer.service;


import org.catapano.truelayer.client.TranslationClient;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TranslationServiceMockingClientTest {
    private final TranslationClient translationClient = mock(TranslationClient.class);
    private final TranslationService service = new TranslationService(translationClient);

    @Test
    void shouldReturnEmptyWhenDescriptionIsNull() {
        Optional<String> description = service.translateDescription(null, TranslationStyle.YODA);

        assertTrue(description.isEmpty());
        verifyNoInteractions(translationClient);
    }

    @Test
    void shouldReturnEmptyWhenDescriptionIsBlank() {
        Optional<String> description = service.translateDescription("   ", TranslationStyle.SHAKESPEARE);

        assertTrue(description.isEmpty());
        verifyNoInteractions(translationClient);
    }

    @Test
    void shouldCallYodaClientWhenStyleIsYoda() {
        String outputTranslate = "Force be with you";
        String inputTranslate = "hello";
        when(translationClient.getTranslationToYoda(inputTranslate))
                .thenReturn(Optional.of(outputTranslate));

        Optional<String> out = service.translateDescription(inputTranslate, TranslationStyle.YODA);

        assertEquals(Optional.of(outputTranslate), out);
        verify(translationClient).getTranslationToYoda(inputTranslate);
        verify(translationClient, never()).getTranslationToShakespeare(anyString());
    }

    @Test
    void shouldCallShakespeareClientWhenStyleIsShakespeare() {
        String outputTranslate="Valorous morrow to thee,  sir";
        String inputTranslate = "hello";
        when(translationClient.getTranslationToShakespeare(inputTranslate))
                .thenReturn(Optional.of(outputTranslate));

        Optional<String> out = service.translateDescription(inputTranslate, TranslationStyle.SHAKESPEARE);

        assertEquals(Optional.of(outputTranslate), out);
        verify(translationClient).getTranslationToShakespeare(inputTranslate);
        verify(translationClient, never()).getTranslationToYoda(anyString());
    }

    @Test
    void shouldReturnEmptyWhenClientReturnsEmpty() {
        String inputTranslate = "hello";
        when(translationClient.getTranslationToYoda(inputTranslate))
                .thenReturn(Optional.empty());

        Optional<String> out = service.translateDescription(inputTranslate, TranslationStyle.YODA);

        assertTrue(out.isEmpty());
        verify(translationClient).getTranslationToYoda(inputTranslate);
    }

}