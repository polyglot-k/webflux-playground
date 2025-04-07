package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private String id;
    private String email;
    private String name;
    public static UserResponse from(User user){
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }
}
