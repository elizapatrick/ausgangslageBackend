package com.ausganslage.ausgangslageBackend.config;

import com.ausganslage.ausgangslageBackend.model.UserAccount;
import com.ausganslage.ausgangslageBackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataLoader initializes the database with two default users: "eliza" and
 * "admin"
 */
@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        try {
            // Only seed if database is empty
            if (userRepository.count() > 0) {
                return;
            }

            // Create two default users
            createUser("eliza", "eliza123");
            createUser("admin", "admin123");

        } catch (Exception e) {
            System.err.println("Error loading initial data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to create and save a user
     */
    private UserAccount createUser(String username, String password) {
        try {
            UserAccount user = new UserAccount();
            user.setUsername(username);
            user.setPassword(password);
            UserAccount saved = userRepository.save(user);
            System.out.println("Created user: " + username);
            return saved;
        } catch (Exception e) {
            System.err.println("Error creating user " + username + ": " + e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }
}
