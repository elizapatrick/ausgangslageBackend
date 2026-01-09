package com.ausganslage.ausgangslageBackend.service;

import com.ausganslage.ausgangslageBackend.exception.InvalidCredentialsException;
import com.ausganslage.ausgangslageBackend.exception.UserNotFoundException;
import com.ausganslage.ausgangslageBackend.model.UserAccount;
import com.ausganslage.ausgangslageBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service for user authentication and management
 * Handles login logic with proper exception handling
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates user with username and password
     * 
     * @throws InvalidCredentialsException if credentials are invalid
     * @throws UserNotFoundException       if user doesn't exist
     */
    public UserAccount authenticate(String username, String password)
            throws InvalidCredentialsException, UserNotFoundException {

        if (username == null || username.trim().isEmpty()) {
            throw new InvalidCredentialsException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidCredentialsException("Password cannot be empty");
        }

        try {
            UserAccount user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            if (!user.getPassword().equals(password)) {
                throw new InvalidCredentialsException("Invalid password for user: " + username);
            }

            return user;
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCredentialsException("Authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get user by ID
     * 
     * @throws UserNotFoundException if user doesn't exist
     */
    public UserAccount getUserById(Long userId) throws UserNotFoundException {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UserNotFoundException("Error retrieving user: " + e.getMessage(), e);
        }
    }
}
