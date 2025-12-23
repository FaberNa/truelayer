package org.catapano.truelayer.config;

import jakarta.servlet.http.HttpServletRequest;
import org.catapano.truelayer.exception.PokemonDescriptionNotFoundException;
import org.catapano.truelayer.exception.PokemonNameNotFoundException;
import org.catapano.truelayer.exception.PokemonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler({PokemonNotFoundException.class, PokemonNameNotFoundException.class})
    public ResponseEntity<ProblemDetail> handlePokemonNotFound(
            PokemonNotFoundException ex,
            HttpServletRequest request
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Pokemon not found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("errorCode", "POKEMON_NOT_FOUND");
        pd.setProperty("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(PokemonDescriptionNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePokemonDescriptionNotFound(
            PokemonNotFoundException ex,
            HttpServletRequest request
    ) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Pokemon description not found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("errorCode", "POKEMON_DESCRIPTION_NOT_FOUND");
        pd.setProperty("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }


    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ProblemDetail> handleUpstreamUnavailable(
            WebClientRequestException ex,
            HttpServletRequest request
    ) {
        log.error("Upstream service unavailable when call :"+ ex.getUri());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Upstream service unavailable");
        pd.setDetail("Failed to reach an external service");
        pd.setProperty("errorCode", "UPSTREAM_UNAVAILABLE");
        pd.setProperty("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }

}
