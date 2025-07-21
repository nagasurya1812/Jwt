package jwt.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jwt.demo.entity.Student;
import jwt.demo.security.jwtsecurity;

@RestController
@RequestMapping("/auth")
public class Authcontroller {
	@Autowired
	private AuthenticationManager authmanager;
    @Autowired
    private jwtsecurity jwt;
    @PostMapping("/post")
    public ResponseEntity<Map<String,String>> login(@RequestBody Student student){
    	try {
    		Authentication authentication=authmanager.authenticate(new UsernamePasswordAuthenticationToken(student.getName(),student.getPassword()));
        	UserDetails user=(UserDetails)authentication.getPrincipal();
        	String token=jwt.generatetoken(user);
        	return ResponseEntity.ok(Map.of("token",token));
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid username or password"));
    	}
    	
    	
    }
    
}
