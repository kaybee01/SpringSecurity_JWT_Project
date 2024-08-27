package org.scaler.securityjwt.ExceptionAdvice;

import io.jsonwebtoken.JwtException;
import org.scaler.securityjwt.Exception.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDTO> handleAuthenticationException(AuthenticationException ex){
        ExceptionDTO exceptionDTO =  new ExceptionDTO();

        exceptionDTO.setMessage(ex.getLocalizedMessage());
        exceptionDTO.setStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(exceptionDTO , HttpStatus.UNAUTHORIZED);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionDTO> handleJWTException(JwtException ex){
        ExceptionDTO exceptionDTO =  new ExceptionDTO();

        exceptionDTO.setMessage(ex.getLocalizedMessage());
        exceptionDTO.setStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(exceptionDTO , HttpStatus.UNAUTHORIZED);

    }

}
