package org.scaler.securityjwt.Exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ExceptionDTO {

    private LocalDateTime timStamp;
    private String message;
    private HttpStatus status;

    public  ExceptionDTO(){
        this.timStamp = LocalDateTime.now();
    }
}
