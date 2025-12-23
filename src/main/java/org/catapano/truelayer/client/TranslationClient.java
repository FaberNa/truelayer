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
        return getTranslation(description, "yoda");
    }


    public Optional<String> getTranslationToShakespeare(String description) {
        return getTranslation(description, "shakespeare");
    }

    private Optional<String> getTranslation(String description, String translationType) {
        try {
            TranslationResponse response = webClient.post()
                    .uri("/translate/" + translationType + ".json")
                    .body(BodyInserters.fromFormData("text", description))
                    .retrieve()
                    .bodyToMono(TranslationResponse.class)
                    .block();

            return Optional.ofNullable(response)
                    .map(TranslationResponse::contents)
                    .map(TranslationResponse.Contents::translated);

        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }

}