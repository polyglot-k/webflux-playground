package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.MemberRequest;
import org.example.dto.MemberResponse;
import org.example.model.Member;
import org.example.repository.MemberRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Mono<MemberResponse> create(MemberRequest request){
        Member member = Member.create(
                request.getName(),
                request.getEmail(),
                request.getNickname(),
                request.getAge());
        return memberRepository.save(member)
                .map(MemberResponse::from);
    }

    public Flux<MemberResponse> retrieveAll(){
        return memberRepository.findAll()
                .map(MemberResponse::from);
    }

    public Mono<MemberResponse> retrieveOneByEmail(String email){
        return memberRepository.findByEmail(email)
                // switchIfEmpty 는 데이터가 empty 일 때 해당 부분으로 들어옴
                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
                .map(MemberResponse::from);
    }

    public Mono<MemberResponse> update(String id, MemberRequest request){
        return memberRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("not found")))
                .flatMap(existingUser->{
                    existingUser.setName(request.getName());
                    existingUser.setEmail(request.getEmail());
                    existingUser.setNickname(request.getNickname());
                    existingUser.setAge(request.getAge());
                    return memberRepository.save(existingUser)
                            .map(MemberResponse::from);
                });
    }

    public Mono<Void> delete(String id){
        return memberRepository.deleteById(id);
    }
}
