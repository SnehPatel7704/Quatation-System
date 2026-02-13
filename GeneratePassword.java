import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        
        // Test the hash
        boolean matches = encoder.matches(password, hash);
        System.out.println("Matches: " + matches);
        
        // Test with the hash from SQL
        String sqlHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        boolean sqlMatches = encoder.matches(password, sqlHash);
        System.out.println("SQL Hash Matches: " + sqlMatches);
    }
}
