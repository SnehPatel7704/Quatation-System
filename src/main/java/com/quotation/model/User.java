package com.quotation.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;
    
    public enum Role {
        SUPERADMIN, ADMIN, USER
    }
}
