package org.catapano.truelayer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.catapano.truelayer.domain.Pokemon;
import org.catapano.truelayer.service.PokemonService;
import org.catapano.truelayer.service.PokemonTranslationService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pokemon", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pokemon", description = "Pokemon retrieval APIs")

public class PokemonController {

    private static final Logger log =
            LoggerFactory.getLogger(PokemonController.class);
    private final PokemonService pokemonService;

    private final PokemonTranslationService pokemonTranslationService;

    public PokemonController(PokemonService pokemonService, PokemonTranslationService pokemonTranslationService) {
        this.pokemonService = pokemonService;
        this.pokemonTranslationService = pokemonTranslationService;
    }


    @GetMapping("/{name}")
    @Operation(
            summary = "Get Pokémon info",
            description = "Retrieves Pokémon information from the upstream Pokémon GraphQL API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pokémon found",
                    content = @Content(schema = @Schema(implementation = Pokemon.class))),
            @ApiResponse(responseCode = "404", description = "Pokémon not found",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "503", description = "Upstream service unavailable",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    public Pokemon getPokemon(
            @Parameter(description = "Pokémon name (case-insensitive)", example = "mewtwo", required = true)
            @PathVariable String name
    ) {
        log.debug("Fetching pokemon info: name={}", name);

        return pokemonService.getPokemonInfo(name);
    }

    @GetMapping("/translated/{name}")
    @Operation(
            summary = "Get Pokémon info with translated description",
            description = "Retrieves Pokémon info and, when applicable, returns a translated description (best-effort)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pokémon found (translation may be original if translation is unavailable)",
                    content = @Content(schema = @Schema(implementation = Pokemon.class))),
            @ApiResponse(responseCode = "404", description = "Pokémon not found",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "503", description = "Upstream service unavailable",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    public Pokemon getPokemonTranslated(
            @Parameter(description = "Pokémon name (case-insensitive)", example = "dunsparce", required = true)
            @PathVariable String name
    ) {
        log.debug("Fetching pokemon translation: name={}", name);
        return pokemonTranslationService.getPokemonWithTranslatedDescription(name);
    }

}
