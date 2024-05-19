package com.nazmul_anik.login_with_jwt.controller;

import com.nazmul_anik.login_with_jwt.entities.AuthRequest;
import com.nazmul_anik.login_with_jwt.entities.UserInfo;
import com.nazmul_anik.login_with_jwt.logout.BlackList;
import com.nazmul_anik.login_with_jwt.service.JwtService;
import com.nazmul_anik.login_with_jwt.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    private BlackList blackList;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to Spring Security with JWT !!";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserInfo userInfo){
        return userInfoService.addUser(userInfo);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest){
       Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

       if(authenticate.isAuthenticated()){
           return jwtService.generateToken(authRequest.getUserName());
       }else{
           throw new UsernameNotFoundException("Invalid user request.");
       }

    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER_ROLES') or hasAuthority('ADMIN_ROLES')")

    public String logout(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
        }
        blackList.blackListToken(token);
        return "You have successfully logged out !!";
    }



    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('USER_ROLES') or hasAuthority('ADMIN_ROLES')")
    public List<UserInfo> getAllUsers(){
        return userInfoService.getAllUser();
    }

    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public UserInfo getUser(@PathVariable Integer id){
        return userInfoService.getUser(id);
    }
}
