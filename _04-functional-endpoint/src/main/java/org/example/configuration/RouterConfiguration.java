package org.example.configuration;

import org.example.handler.TodoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> todoRoute(TodoHandler handler) {
        return RouterFunctions
                .nest(path("/todos"),
                        RouterFunctions.route(GET(""), handler::findAll)
                                .andRoute(GET("/{id}"), handler::findById)
                                .andRoute(POST(""), handler::create)
                                .andRoute(PUT("/{id}"), handler::update)
                                .andRoute(DELETE("/{id}"), handler::delete)
                );
    }
}
