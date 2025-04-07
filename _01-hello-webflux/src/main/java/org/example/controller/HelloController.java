package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class HelloController {

    // Mono: 1개 결과
    @GetMapping("/hello")
    public Mono<String> sayHello() {
        return Mono.just("Hello, WebFlux!");
        // Mono.just(...) 의 형식은 단일 데이터를 즉시 Emit 후 완료(단일 스트림)
    }

    // Flux: 여러 개 결과
    @GetMapping("/numbers")
    public Flux<Integer> numbers() {
        return Flux.range(1, 5); // 1~5까지 숫자 반환
        //**"반복 처리"나 "테스트/디버깅", "시뮬레이션", "제어 흐름"**에 자주 사용됨.
    }

    // Flux with delay (streaming 느낌)
    @GetMapping("/stream")
    public Flux<String> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> "Stream - " + i)
                .take(5); // 5개까지만 전송
        // 초당 1씩 더해지는 internal 에 map 으로 문자열 넣어서 총 5개만 ㅅ전송
        // 주로 SSE 의 형식에서 많이 쓰임. (text/stream)
        // Client 에 일정 주기로 heartbeat를 보내는 용도로도 사용
        // WARNINGS: take가 없으면 무한정 반복됨
    }

}
