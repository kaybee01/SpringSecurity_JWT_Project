package org.scaler.securityjwt.DTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
