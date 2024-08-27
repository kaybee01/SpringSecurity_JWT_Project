package org.scaler.securityjwt.Service;

import org.scaler.securityjwt.Model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService   {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    AuthService(AuthenticationManager authenticationManager,JWTService jwtService){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }

    public String login(String email, String password){
        Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        User u = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(u);
        return token;
    }
}
