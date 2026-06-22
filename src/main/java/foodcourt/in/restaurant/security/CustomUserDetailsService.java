package foodcourt.in.restaurant.security;

import foodcourt.in.restaurant.entity.User;
import foodcourt.in.restaurant.exception.ResourceNotFoundException;
import foodcourt.in.restaurant.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Email do not Exist"));

        SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_"+user.getRole());
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),Collections.singletonList(authority));
    }
}
