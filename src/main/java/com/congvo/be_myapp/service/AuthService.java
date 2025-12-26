package com.congvo.be_myapp.service;

import com.congvo.be_myapp.dto.request.LoginRequest;
import com.congvo.be_myapp.dto.request.SignUpRequest;
import com.congvo.be_myapp.entity.PasswordResetToken;
import com.congvo.be_myapp.entity.Role;
import com.congvo.be_myapp.entity.User;
import com.congvo.be_myapp.repository.PasswordResetTokenRepository;
import com.congvo.be_myapp.repository.RoleRepository;
import com.congvo.be_myapp.repository.UserRepository;
import com.congvo.be_myapp.util.JwtUtil;
import com.congvo.be_myapp.util.StringUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final StringUtil stringUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthService(JwtUtil jwtUtil,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       RoleRepository roleRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       EmailService emailService,
                       StringUtil stringUtil
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.stringUtil = stringUtil;
    }

    public String register(SignUpRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new RuntimeException("Error: phone number is already in use!");
        }

        if (!stringUtil.isValidPassword(signUpRequest.getPassword())) {
            throw new RuntimeException("Error: Password must be at least 8 characters long!");
        }

        User user = new User();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setUsername(signUpRequest.getUsername());
        user.setPhoneNumber(String.valueOf(signUpRequest.getPhoneNumber()));
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        userRepository.save(user);

        return "User registered successfully!";
    }

    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail());

        return jwtUtil.generateToken(String.valueOf(user.getId()), loginRequest.getEmail(), user.getUsername());
    }

    public String generateTokenOnly(String email) {
        User user = userRepository.findByEmail(email);
        return jwtUtil.generateToken(String.valueOf(user.getId()), email, user.getUsername());
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "If your email exists, a reset link has been sent.";
        }

        passwordResetTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
        return "If your email exists, a reset link has been sent.";
    }

    public String resetPassword(String token, String newPassword) {
        if (!stringUtil.isValidPassword(newPassword)) {
            throw new RuntimeException("Error: Password must be at least 8 characters long!");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Consume the token so it can't be used again
        passwordResetTokenRepository.delete(resetToken);

        return "Password successfully reset";
    }

}
