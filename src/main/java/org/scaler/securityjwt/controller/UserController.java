package org.scaler.securityjwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.scaler.securityjwt.DTO.LoginRequestDTO;
import org.scaler.securityjwt.DTO.LoginResponseDTO;
import org.scaler.securityjwt.DTO.SignUpRequestDTO;
import org.scaler.securityjwt.DTO.UserDTO;
//import org.scaler.securityjwt.Model.Token;
import org.scaler.securityjwt.Model.User;
import org.scaler.securityjwt.Service.AuthService;
import org.scaler.securityjwt.Service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserServiceImpl userService;
    private AuthService authService;
    UserController(UserServiceImpl userService,AuthService authService){
        this.userService=userService;
        this.authService=authService;
    }

    @PostMapping("/signup")
    public UserDTO signup(@RequestBody SignUpRequestDTO requestDTO){
        User u = userService.signUp(requestDTO.getName(),requestDTO.getPassword(),requestDTO.getEmail());
        return UserDTO.from(u);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO , HttpServletRequest request, HttpServletResponse response){
        LoginResponseDTO loginResponseDTO = authService.login(requestDTO.getEmail() , requestDTO.getPassword());

        Cookie cookie = new Cookie("refreshToken" , loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(loginResponseDTO , HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationServiceException("No cookies found in the request");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        LoginResponseDTO loginResponseDto = authService.refreshToken(refreshToken);
        System.out.println("Refresh token received from cookies: " + refreshToken);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK); // Return OK status on success
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationServiceException("No cookies found in the request");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));


        authService.logout(refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
