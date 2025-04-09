package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com") // example URL
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("➡️ 요청: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("⬅️ 응답 상태: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
