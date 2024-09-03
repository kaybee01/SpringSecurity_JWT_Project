package org.scaler.securityjwt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

//    private String value;
//    private Date expiry;
//    private Boolean isValid;
//
//    public static LoginResponseDTO from( Token t){
//        LoginResponseDTO responseDTO =  new LoginResponseDTO();
//
//        responseDTO.setExpiry(t.getExpiry());
//        responseDTO.setValue(t.getValue());
//        responseDTO.setIsValid(t.getIsValid());
//        return  responseDTO;
//    }

    private Long id;
    private String accessToken;
    private String refreshToken;


}
