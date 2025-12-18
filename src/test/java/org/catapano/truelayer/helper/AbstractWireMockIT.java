package org.catapano.truelayer.helper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public abstract class AbstractWireMockIT {

    @RegisterExtension
    static WireMockExtension wmPokemon = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @RegisterExtension
    static WireMockExtension wmTranslate = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();


    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("client.pokemon.uri",  wmPokemon::baseUrl);
        r.add("client.translate.uri",  wmTranslate::baseUrl);
    }

    protected PokemonApiStubs stubsPokemonApi() {
        return new PokemonApiStubs(wmPokemon);
    }

    protected TranslationStubs stubsTranslationApi() {
        return new TranslationStubs(wmTranslate);
    }

    @BeforeEach
    void resetWireMock() {
        wmPokemon.resetAll();
        wmTranslate.resetAll();
    }
}