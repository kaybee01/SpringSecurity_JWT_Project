package org.scaler.securityjwt.DTO;

import lombok.Data;
import org.scaler.securityjwt.Model.Role;
import org.scaler.securityjwt.Model.User;

import java.util.List;

@Data
public class UserDTO {
    //private Long id;
    private String email;
    private String name;
    private List<Role> roles;

    public static UserDTO from(User u){
        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(u.getEmail());
        userDTO.setName(u.getName());
        return userDTO;
    }
}
