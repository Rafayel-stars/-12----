package org.example.bookmanagesystem01.service;

import org.example.bookmanagesystem01.entity.User;
import org.example.bookmanagesystem01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String no) throws UsernameNotFoundException {
        // 仅通过 no 查询用户
        Optional<User> userOptional = userRepository.findByNo(no);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                user.getNo(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType()))
        );
    }
}