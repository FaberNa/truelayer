package org.catapano.truelayer.helper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PokemonApiStubs {

    private final WireMockExtension wm;

    public PokemonApiStubs(WireMockExtension wm) {
        this.wm = wm;
    }

    public void stubPokemonSpeciesOk(String name, boolean isLegendary, String habitat, String flavorText) {
        wm.stubFor(post(urlEqualTo("/v1beta2"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.query", containing("samplePokeAPIquery")))
                .withRequestBody(matchingJsonPath("$.variables.name", equalTo(name)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(graphqlResponse(name, isLegendary, habitat, flavorText))));
    }

    public void stubPokemonSpeciesEmptyResult(String name) {
        wm.stubFor(post(urlEqualTo("/v1beta2"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.variables.name", equalTo(name)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                { "data": { "pokemon_detail": [] } }
                """)));
    }

    public void stubPokemonSpeciesWithoutDescription(String name, boolean isLegendary, String habitat) {
        wm.stubFor(post(urlEqualTo("/v1beta2"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.variables.name", equalTo(name)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                {
                  "data": {
                    "pokemon_detail": [
                      {
                        "name": "%s",
                        "is_legendary": %s,
                        "pokemonhabitat": {
                          "name": "%s"
                        },
                        "description": []
                      }
                    ]
                  }
                }
                """.formatted(
                                        escapeJson(name),
                                        isLegendary,
                                        escapeJson(habitat)
                                )
                        )));
    }

    public void stubGraphqlServerError() {
        wm.stubFor(post(urlEqualTo("/v1beta2"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                { "errors": [ { "message": "Internal error" } ] }
                                """)));
    }

    public void verifyCalledFor(String name) {
        wm.verify(postRequestedFor(urlEqualTo("/v1beta2"))
                .withRequestBody(containing("\"_eq: \\\"" + name + "\\\"\"")));
    }

    private static String graphqlResponse(String name, boolean isLegendary, String habitat, String flavorText) {
        return """
                {
                  "data": {
                    "pokemon_detail": [
                      {
                        "name": "%s",
                        "is_legendary": %s,
                        "pokemonhabitat": { "name": "%s" },
                        "description": [ { "flavor_text": "%s" } ]
                      }
                    ]
                  }
                }
                """.formatted(
                escapeJson(name),
                isLegendary,
                escapeJson(habitat),
                escapeJson(flavorText)
        );
    }

    private static String escapeJson(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\t", "\\t");
    }
}
