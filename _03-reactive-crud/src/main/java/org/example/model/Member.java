package org.example.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "members")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
    @Id
    private String id;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String nickname;
    @Setter
    private int age;

    public static Member create(String name, String email, String nickname, int age){
        return Member.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .age(age)
                .build();
    }
}
