package org.scaler.securityjwt.DTO;

import lombok.Data;
import org.scaler.securityjwt.Model.Token;

import java.util.Date;

@Data
public class LoginResponseDTO {

    private String value;
    private Date expiry;
    private Boolean isValid;

    public static LoginResponseDTO from( Token t){
        LoginResponseDTO responseDTO =  new LoginResponseDTO();

        responseDTO.setExpiry(t.getExpiry());
        responseDTO.setValue(t.getValue());
        responseDTO.setIsValid(t.getIsValid());
        return  responseDTO;
    }

}
