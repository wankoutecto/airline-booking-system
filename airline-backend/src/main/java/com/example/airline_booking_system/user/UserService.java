package com.example.airline_booking_system.user;

import com.example.airline_booking_system.auth.dto.RegisterRequest;
import com.example.airline_booking_system.common.exception.ResourceAlreadyExistsException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.security.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Email already exists.");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        String hashPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashPassword);
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);
       return userRepository.save(user);
    }

    public User findUserById(Long tokenUserId) {
        return userRepository.findById(tokenUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updateUserAuthorization(Long userId, Role role){
        User user = findUserById(userId);
        user.setRole(role);
        userRepository.save(user);
    }

}
