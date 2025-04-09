package org.example;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SseHandler {

    private final SseEmitterPool pool;

    // /sse/{userId}
    public Mono<ServerResponse> subscribe(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Flux<SseEvent> stream = pool.subscribe(userId);

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(stream, SseEvent.class);
    }

    // /send/{userId}
    public Mono<ServerResponse> send(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return request.bodyToMono(String.class)
                .doOnNext(msg -> {
                    pool.sendTo(userId, new SseEvent(msg, System.currentTimeMillis()));
                })
                .then(ServerResponse.ok().build());
    }

    // /sse/interval/{userId}
    public Mono<ServerResponse> interval(ServerRequest request) {
        Flux<SseEvent> flux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new SseEvent("ping #" + i, System.currentTimeMillis()));

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(flux, SseEvent.class);
    }
}