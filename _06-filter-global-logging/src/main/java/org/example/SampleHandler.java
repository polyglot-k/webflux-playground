package org.example;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SampleHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return request.bodyToMono(HelloRequest.class)
                .flatMap(body -> {
                    HelloResponse response = new HelloResponse("Hello, " + body.getName());
                    return ServerResponse.ok().bodyValue(response);
                });
    }
}
