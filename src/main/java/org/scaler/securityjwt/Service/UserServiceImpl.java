package org.scaler.securityjwt.Service;

import org.apache.commons.lang3.RandomStringUtils;

import org.scaler.securityjwt.Model.User;
import org.scaler.securityjwt.Config.UserConfig;
//import org.scaler.securityjwt.Repo.TokenRepo;
import org.scaler.securityjwt.Repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserDetailsService {

    private BCryptPasswordEncoder encoder;
    private UserRepo userRepo;
//    private TokenRepo tokenRepo;
//    private AuthenticationManager authenticationManager;
//    private JWTService jwtService;

    UserServiceImpl(BCryptPasswordEncoder encoder,UserRepo userRepo){
        this.encoder=encoder;
        this.userRepo=userRepo;
      //  this.tokenRepo=tokenRepo;
//        this.authenticationManager=authenticationManager;
//        this.jwtService=jwtService;
    }

    public User signUp(String name, String password, String email) {
        User u = new User();
        Optional<User> user = userRepo.findByEmail(email);
         if(!user.isEmpty()){
             throw new BadCredentialsException("User with "+email+" alrady exist..!");
         }
        u.setName(name);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setIsverified(false);
        userRepo.save(u);
        return u;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
//        return userRepo.findByEmail(username)
//                .orElseThrow(() -> new BadCredentialsException("User with email "+ username +" not found"));
        Optional<User> u = userRepo.findByEmail(username);

        if(u.isEmpty()){
            throw new BadCredentialsException("User with email "+ username +" not found");
        }

        return u.get();

    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with id "+ userId +
                " not found"));
    }

    public User getUsrByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User with mail "+ email + " not found"));
    }


    //@Override
//    public Token login(String email, String password) {
//
//        Optional<User> userOptional = userRepo.findByEmail(email);
//
//        if(userOptional.isEmpty()){
//            throw new RuntimeException("Invalid UserName");
//        }
//        if(!encoder.matches(password,userOptional.get().getPassword())){
//            throw  new RuntimeException("Invalid UserName..!");
//        }
//
//
//        Token t = generateToken(userOptional.get());
//        tokenRepo.save(t);
//        return t;
//
//
//    }
//
//    public Token generateToken(User u){
//        LocalDate currentDate =LocalDate.now();
//        LocalDate thirtyDaysLater =currentDate.plusDays(30);
//        Date expiry = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Token t = new Token();
//        t.setExpiry(expiry);
//        t.setIsValid(true);
//        t.setUser(u);
//        t.setValue(RandomStringUtils.randomAlphanumeric(128));
//        return t;


}
