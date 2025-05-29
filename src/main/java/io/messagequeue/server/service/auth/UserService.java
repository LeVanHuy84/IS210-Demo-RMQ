package io.messagequeue.server.service.auth;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.messagequeue.server.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
