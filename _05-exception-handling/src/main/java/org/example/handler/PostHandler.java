package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.dto.PostRequest;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class PostHandler {
    private final PostRepository postRepository;

   public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(PostRequest.class)
                .map(postRequest -> Post.create(postRequest.getTitle(), postRequest.getContent(), postRequest.getUserId()))
                .flatMap(postRepository::save)
                .flatMap(post -> ServerResponse.ok().body(fromValue(post)));
    }
    public Mono<ServerResponse> retrieveByUserId(ServerRequest request){
        String userId = request.queryParam("user_id")
                .orElseThrow(IllegalArgumentException::new);

        return ServerResponse.ok()
                .body(postRepository.findAllByUserId(userId), Post.class);
    }
}
