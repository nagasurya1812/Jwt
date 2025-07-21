package jwt.demo.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.demo.entity.Student;
import jwt.demo.repository.StudentRepository;
import jwt.demo.services.CustomUserDetails;
@Component
public class JwtFilter extends OncePerRequestFilter{
	@Autowired
	jwtsecurity jwt;
	@Autowired
	CustomUserDetails custom_users;
    @Autowired
    StudentRepository repository;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header=request.getHeader("Authorization");
		if(header==null || !header.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token=header.substring(7);
		try { 
			 String name = jwt.extractusername(token);

		        if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		            UserDetails user = custom_users.loadUserByUsername(name);
		            Student student = repository.findByName(user.getUsername());

		            if (jwt.validatetoken(token, student)) {
		                UsernamePasswordAuthenticationToken authToken =
		                        new UsernamePasswordAuthenticationToken(student.getName(),student.getPassword() , user.getAuthorities());
		                SecurityContextHolder.getContext().setAuthentication(authToken);
		                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                SecurityContextHolder.getContext().setAuthentication(authToken);
		            }
		        }
			
		}catch(Exception e) {
			Map<String,String> respons=new HashMap<>();
			respons.put("Error", "Invalid token");
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(respons);
			
			
			response.getWriter().write(jsonString);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		filterChain.doFilter(request, response);
		
	}

}
