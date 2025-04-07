package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    private String userId;
    public static Post create(String title, String content, String userId){
        return Post.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .build();
    }
}
