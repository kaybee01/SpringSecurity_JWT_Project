package org.scaler.securityjwt.Service;

import jakarta.servlet.http.Cookie;
import org.scaler.securityjwt.DTO.LoginResponseDTO;
import org.scaler.securityjwt.Model.Session;
import org.scaler.securityjwt.Model.User;
import org.scaler.securityjwt.Repo.SessionRepo;
import org.scaler.securityjwt.Repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService   {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private UserServiceImpl userService;
    private SessionService sessionService;
    private UserRepo userRepo;
    private final SessionRepo sessionRepo;

    AuthService(AuthenticationManager authenticationManager,JWTService jwtService,UserServiceImpl userService,SessionService sessionService,UserRepo userRepo,
                SessionRepo sessionRepo){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.userService=userService;
        this.sessionService=sessionService;
        this.userRepo=userRepo;
        this.sessionRepo = sessionRepo;
    }

    public LoginResponseDTO login(String email, String password){
        Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        User u = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(u);
        String  refreshToken = jwtService.generateRefreshToken(u);
        sessionService.generateSession(u , refreshToken);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(u.getId() , accessToken , refreshToken);
        return loginResponseDTO;
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        if (userId == null) {
            throw new AuthenticationServiceException("Invalid refresh token");
        }

        //To validate refreshToken in if it is valid or not
        sessionService.validateSession(refreshToken);

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with ID: " + userId);
        }

        String accessToken = jwtService.generateAccessToken(user);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId() , accessToken , refreshToken);
        return loginResponseDTO;
    }

    public void logout(String refreshToken){
        Optional<Session> session = sessionRepo.findByRefreshToken(refreshToken);
        if(!session.isEmpty()){
            sessionRepo.delete(session.get());
        }
        throw new RuntimeException("Invalid Token");
    }
}
