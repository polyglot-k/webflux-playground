package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequest;
import org.example.dto.UserResponse;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping
    public Mono<UserResponse> create(@RequestBody UserRequest request){
        return service.create(request);
    }
    @GetMapping
    public Flux<UserResponse> retrieveAll(){
        return service.retrieveAll();
    }


}
