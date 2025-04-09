package org.example;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Order(-1)
public class AdvancedLoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest decoratedRequest = decorateRequest(exchange);
        ServerHttpResponse decoratedResponse = decorateResponse(exchange);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(decoratedRequest)
                .response(decoratedResponse)
                .build();

        return chain.filter(mutatedExchange);
    }

    private ServerHttpRequestDecorator decorateRequest(ServerWebExchange exchange) {
        ServerHttpRequest original = exchange.getRequest();
        return new ServerHttpRequestDecorator(original) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    log.info("📥 [REQ] {} {} : {}", original.getMethod(), original.getURI(), body);
                });
            }
        };
    }

    private ServerHttpResponseDecorator decorateResponse(ServerWebExchange exchange) {
        ServerHttpResponse originalResponse = exchange.getResponse();

        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux<? extends DataBuffer> flux) {

                    return super.writeWith(
                            flux.doOnNext(buffer -> {
                                byte[] bytes = new byte[buffer.readableByteCount()];
                                buffer.read(bytes);
                                String responseBody = new String(bytes, StandardCharsets.UTF_8);
                                HttpStatus status = (HttpStatus) getStatusCode();
                                log.info("📤 [RES] {} [status: {}] → {}", exchange.getRequest().getPath(), status, responseBody);
                            })
                    );
                }
                return super.writeWith(body);
            }
        };
    }
}