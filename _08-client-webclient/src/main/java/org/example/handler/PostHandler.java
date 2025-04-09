package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class PostHandler {
    private final WebClient webClient;
    public Mono<ServerResponse> fetchPostAsString(ServerRequest request){
        String id = request.pathVariable("id");


        Mono<String> responseBody = webClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
        return responseBody.flatMap(response->
                    ServerResponse.ok()
                            .header("Content-Type","application/json")
                            .bodyValue(response)
                );
    }
}
