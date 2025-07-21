package jwt.demo.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jwt.demo.entity.Student;
import jwt.demo.repository.StudentRepository;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private StudentRepository repository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Student user = repository.findByName(name);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User(
            user.getName(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
