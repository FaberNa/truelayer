package org.catapano.truelayer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
@Configuration
public class WebClientConfig {


    @Value("${client.translate.uri}")
    String uriTranslate;
    @Value("${client.translate.connectTimeout:500}")
    int connectTimeoutTranslate;
    @Value("${client.translate.readTimeout:1500}")
    int readTimeoutTranslate;

    @Value("${client.pokemon.uri}")
    String urlPokemon;
    @Value("${client.pokemon.connectTimeout:500}")
    int connectTimeoutPokemon;
    @Value("${client.pokemon.readTimeout:1500}")
    int readTimeoutPokemon;

    @Bean
    WebClient funTranslationsWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(readTimeoutTranslate))   // timeout response
                // connect timeout (ms)
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutTranslate);
        return WebClient.builder()
                .baseUrl(uriTranslate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    WebClient pokemonInfoWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(readTimeoutPokemon))   // timeout response
                // connect timeout (ms)
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutPokemon);
        return WebClient.builder()
                .baseUrl(urlPokemon)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}