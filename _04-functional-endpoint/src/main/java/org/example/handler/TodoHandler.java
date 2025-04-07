package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.model.Todo;
import org.example.repository.TodoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class TodoHandler {
    private final TodoRepository repository;
    public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(Todo.class)
                .flatMap(repository::save)
                .flatMap(saved -> ServerResponse.ok().body(fromValue(saved)));
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Todo> todos = repository.findAll();
        return ServerResponse.ok().body(todos, Todo.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(todo -> ServerResponse.ok().body(fromValue(todo)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<Todo> updatedTodo = request.bodyToMono(Todo.class);

        return repository.findById(id)
                .flatMap(existTodo ->
                    updatedTodo.flatMap(newTodo->{
                        existTodo.setDescription(newTodo.getDescription());
                        existTodo.setStatus(newTodo.getStatus());
                        return repository.save(existTodo);
                    }))
                .flatMap(saved -> ServerResponse.ok().body(fromValue(saved)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(todo->
                        repository.delete(todo)
                                .then(ServerResponse.noContent().build()))
                // then 은 위에 해당 Mono 작업 delete 가 다 끝나고 수행되는 지점임.
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
