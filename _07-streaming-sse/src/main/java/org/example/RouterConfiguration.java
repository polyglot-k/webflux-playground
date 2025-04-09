package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> sseRoutes(SseHandler handler) {
        return RouterFunctions
                .route()
                .GET("/sse/{userId}", handler::subscribe)
                .POST("/send/{userId}", handler::send)
                .GET("/sse/interval/{userId}", handler::interval)
                .build();
    }
}