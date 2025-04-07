package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequest;
import org.example.dto.UserResponse;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public Mono<UserResponse> create(UserRequest request){
        User newUser = User.create(
                request.getEmail(),
                request.getName()
        );
        return repository.save(newUser)
                .map(UserResponse::from);
    }
    public Flux<UserResponse> retrieveAll(){
        return repository.findAll()
                .map(UserResponse::from);
    }
}
