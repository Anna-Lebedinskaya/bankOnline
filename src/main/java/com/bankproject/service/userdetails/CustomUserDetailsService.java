package com.bankproject.service.userdetails;

import com.bankproject.constants.UserRolesConstants;
import com.bankproject.repository.UserRepository;
import com.bankproject.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.bankproject.constants.UserRolesConstants.ADMIN;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String adminUser;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUser)) {
            return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        } else {
            User user = userRepository.findUserByLogin(username);

            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(user.getRole().getId() == 1L
                    ? "ROLE_" + UserRolesConstants.USER
                    : "ROLE_" + UserRolesConstants.MANAGER));
            return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
        }
    }
}
