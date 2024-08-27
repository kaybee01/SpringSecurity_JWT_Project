package org.scaler.securityjwt.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.scaler.securityjwt.Model.User;
import org.scaler.securityjwt.Service.JWTService;
import org.scaler.securityjwt.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserServiceImpl userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    JWTAuthFilter(JWTService jwtService,UserServiceImpl userService){
        this.jwtService=jwtService;
        this.userService=userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
        {
            try{
            final String requestTokenHeader = request.getHeader("Authorization");
            System.out.println("Token: " + requestTokenHeader);

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = requestTokenHeader.substring(7).trim();

            if (token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = null;
            try {
                userId = jwtService.getUserIdfromToken(token);
            } catch (Exception e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        }catch (Exception ex){
                handlerExceptionResolver.resolveException(request,response,null,ex);
            }



    }
}
