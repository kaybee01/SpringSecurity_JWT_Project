package org.scaler.securityjwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.scaler.securityjwt.DTO.LoginRequestDTO;
import org.scaler.securityjwt.DTO.LoginResponseDTO;
import org.scaler.securityjwt.DTO.SignUpRequestDTO;
import org.scaler.securityjwt.DTO.UserDTO;
import org.scaler.securityjwt.Model.Token;
import org.scaler.securityjwt.Model.User;
import org.scaler.securityjwt.Service.AuthService;
import org.scaler.securityjwt.Service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO requestDTO , HttpServletRequest request, HttpServletResponse response){
        String  t = authService.login(requestDTO.getEmail() , requestDTO.getPassword());

        Cookie cookie = new Cookie("token" , t);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(t , HttpStatus.OK);

    }
}
