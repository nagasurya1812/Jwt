package jwt.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestBody;

import jwt.demo.entity.Student;
import jwt.demo.security.JwtFilter;
import jwt.demo.services.CustomUserDetails;

@Configuration
@EnableWebSecurity
public class Securityconfig {
	@Autowired
	JwtFilter jwtfilter;
	  @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 http.authorizeHttpRequests(auth->
    	         auth.
    	         requestMatchers("/students/post").permitAll().
    			 requestMatchers("/students/**").authenticated().
    			 anyRequest().permitAll())
    	 //.formLogin(form->form.permitAll().defaultSuccessUrl("/dashboard",true))
    	  .csrf(csrf->csrf.disable())
    	  .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	  .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
    	 return http.build();
      }
	  @Bean
	  public AuthenticationManager authenticationmanger() {
		  return new ProviderManager(List.of(authenticationprovider()));
	  }

	  @Bean
	  public UserDetailsService userDetailServices() {
		 /* UserDetails admin=User.withUsername("naga").password(passwordencoder.encode("surya123")).roles("admin").build();
		  UserDetails employee=User.withUsername("hari").password(passwordencoder.encode("hari123")).roles("employee").build();
		  return new InMemoryUserDetailsManager(admin,employee);*/
		  return new CustomUserDetails();
	  }
	  @Bean
	  public DaoAuthenticationProvider authenticationprovider() {
		  DaoAuthenticationProvider authprovider = new DaoAuthenticationProvider();
		  authprovider.setUserDetailsService(userDetailServices());
		  authprovider.setPasswordEncoder(passwordEncoder());
		  return authprovider;
	  }
	  @Bean
	  public PasswordEncoder passwordEncoder() {
		  return new BCryptPasswordEncoder();
	  }
}
