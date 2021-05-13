package com.hoanganhbk.schoolBus.service;

import com.hoanganhbk.schoolBus.data.model.CustomUserDetails;
import com.hoanganhbk.schoolBus.data.model.User;
import com.hoanganhbk.schoolBus.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(Long.parseLong(userId));

        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }
        return new CustomUserDetails(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User save(User user) {
        user = userRepository.save(user);

        return user;
    }
}
