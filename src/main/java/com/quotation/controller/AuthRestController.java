package com.quotation.controller;

import com.quotation.config.JwtUtil;
import com.quotation.dto.LoginRequest;
import com.quotation.dto.LoginResponse;
import com.quotation.model.User;
import com.quotation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthRestController(AuthenticationManager authenticationManager, 
                             JwtUtil jwtUtil,
                             UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");

            String token = jwtUtil.generateToken(request.getUsername(), role);
            
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), role, user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse("USER");
            
        return ResponseEntity.ok(new LoginResponse(null, user.getUsername(), role, user.getEmail()));
    }
}
