package org.catapano.truelayer.client;

import org.catapano.truelayer.dto.TranslationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class TranslationClient {


    private final WebClient webClient;

    public TranslationClient(WebClient funTranslationsWebClient) {
        this.webClient = funTranslationsWebClient;
    }

        public Optional<String> getTranslationToYoda(String description) {
            try {
                TranslationResponse response = webClient.post()
                        .uri("/translate/yoda.json")
                        .body(BodyInserters.fromFormData("text", description))
                        .retrieve()
                        .bodyToMono(TranslationResponse.class)
                        .block();

                if (response == null ||
                        response.contents() == null ||
                        response.contents().translated() == null ||
                        response.contents().translated().isBlank()) {
                    return Optional.empty();
                }

                return Optional.of(response.contents().translated());

            } catch (WebClientResponseException e) {
                // other http error → fallback
                return Optional.empty();
            }
        }


    public Optional<String> getTranslationToShakespeare(String description){
        try {
            TranslationResponse response = webClient.post()
                    .uri("/translate/shakespeare.json")
                    .body(BodyInserters.fromFormData("text", description))
                    .retrieve()
                    .bodyToMono(TranslationResponse.class)
                    .block();

            if (response == null ||
                    response.contents() == null ||
                    response.contents().translated() == null ||
                    response.contents().translated().isBlank()) {
                return Optional.empty();
            }

            return Optional.of(response.contents().translated());

        } catch (WebClientResponseException e) {
            // other http error → fallback
            return Optional.empty();
        }
    }

}
