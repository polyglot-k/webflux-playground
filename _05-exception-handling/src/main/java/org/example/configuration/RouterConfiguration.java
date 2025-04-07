package org.example.configuration;

import org.example.handler.PostHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfiguration {
    @Bean
    public RouterFunction<ServerResponse> postRoute(PostHandler handler){
        return RouterFunctions
                .nest(path("/posts"),
                        RouterFunctions.route(GET(""), handler::retrieveByUserId)
                                .andRoute(POST(""), handler::create)
                );
    }
}
