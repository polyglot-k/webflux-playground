package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SampleRouter {

    @Bean
    public RouterFunction<?> routes(SampleHandler handler) {
        return route(POST("/hello"), handler::hello);
    }
}