package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterPool {
    private final Map<String, Sinks.Many<SseEvent>> sinks = new ConcurrentHashMap<>();

    public Flux<SseEvent> subscribe(String userId) {
        Sinks.Many<SseEvent> sink = Sinks.many().multicast().onBackpressureBuffer();
        sinks.put(userId, sink);

        log.info("🟢 {} connected", userId);

        return sink.asFlux()
                .doOnCancel(() -> {
                    sinks.remove(userId);
                    log.info("🔴 {} disconnected", userId);
                });
    }

    public void sendTo(String userId, SseEvent event) {
        Sinks.Many<SseEvent> sink = sinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(event);
            log.info("📤 sent to {}: {}", userId, event);
        } else {
            log.warn("⚠️ no active sink for {}", userId);
        }
    }
}