package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "todos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Todo {
    @Id
    private String id;
    @Setter
    private String description;
    @Setter
    private TodoStatus status;
}
