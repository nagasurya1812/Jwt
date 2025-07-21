package jwt.demo.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jwt.demo.entity.Student;

@Component
public class jwtsecurity {
	private static final String SECRET_KEY="yW1JoVx9E01KqPmdQl44E7r8DwwUmNAF";
	private final SecretKey SECRET=Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    public String generatetoken(UserDetails user) {
    	return Jwts.builder().
    			subject(user.getUsername()).
    			issuedAt(new Date()).
    			expiration(new Date(System.currentTimeMillis()+1000*60*60)).
    			signWith(SECRET,Jwts.SIG.HS256)
    			.compact();
    }
    public boolean validatetoken(String token,Student student) {
    	return extractusername(token).equals(student.getName());
    }
    public String extractusername(String token) {
    	return Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
