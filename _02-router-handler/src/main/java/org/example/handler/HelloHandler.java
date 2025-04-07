package org.example.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HelloHandler {
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().bodyValue("Hello, Router!");
    }

    public Mono<ServerResponse> greet(ServerRequest request) {
        String name = request.queryParam("name").orElse("Anonymous");
        return ServerResponse.ok().bodyValue("Hi " + name + "!");
    }

    public Mono<ServerResponse> echo(ServerRequest request) {
        // Serialize (Body 데이터를 Mono 타입의 DTO로)
        Mono<MessageDTO> messageMono = request.bodyToMono(MessageDTO.class);
        //Mono 는 아직 값이 있는 상태가 아님 flatMap 을 만나면 이를 받아서 비동기 작업을 처리

        return messageMono.flatMap(msg ->
                //Mono/Flux 안의 값을 꺼내서, 비동기 작업을 연결해주는 연산자
                ServerResponse.ok().bodyValue("You said: " + msg.getContent())
        );
    }
}
