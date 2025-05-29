package io.messagequeue.server.service.auth;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.messagequeue.server.dto.auth.RegisterRequest;
import io.messagequeue.server.model.auth.User;
import io.messagequeue.server.model.enums.Role;
import io.messagequeue.server.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username: " + request.getUsername() + " has exist");
            }

            // Tính tuổi của người dùng từ ngày sinh
            LocalDate birthDate = request.getDateOfBirth();
            int userAge = Period.between(birthDate, LocalDate.now()).getYears();

            if (userAge < 12) {
                throw new RuntimeException("User is under the minimum age of " + 12);
            }
            
            User newUser = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .fullname(request.getFullName())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(newUser);
            return "Đây là hàm";
        } catch (RuntimeException e) {
            log.error("Register errors", e);
            throw new RuntimeException("Register errors" + e.getMessage());
        }
    }
}
