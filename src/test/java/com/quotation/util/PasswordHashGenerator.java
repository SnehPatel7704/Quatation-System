package com.quotation.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        
        // Generate 3 hashes to verify consistency
        for (int i = 1; i <= 3; i++) {
            String hash = encoder.encode(password);
            boolean matches = encoder.matches(password, hash);
            System.out.println("Hash " + i + ": " + hash);
            System.out.println("Matches: " + matches);
            System.out.println();
        }
        
        // Test the existing hash from SQL
        String existingHash = "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cyhQQl3MpbEyEW9ihPDPGRlRgM1C2";
        System.out.println("Testing existing hash from SQL:");
        System.out.println("Hash: " + existingHash);
        System.out.println("Matches 'password123': " + encoder.matches(password, existingHash));
    }
}
