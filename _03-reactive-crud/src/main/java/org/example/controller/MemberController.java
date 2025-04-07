package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.MemberRequest;
import org.example.dto.MemberResponse;
import org.example.service.MemberService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;
    @PostMapping
    public Mono<MemberResponse> create(@RequestBody MemberRequest request) {
        return service.create(request);
    }

    @GetMapping
    public Flux<MemberResponse> findAll() {
        return service.retrieveAll();
    }

    @GetMapping("/{id}")
    public Mono<MemberResponse> findOne(@PathVariable String id) {
        return service.retrieveOneByEmail(id);
    }

    @PutMapping("/{id}")
    public Mono<MemberResponse> update(@PathVariable String id, @RequestBody MemberRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }
}
