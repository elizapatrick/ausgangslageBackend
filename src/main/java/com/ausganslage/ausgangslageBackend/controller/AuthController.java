package com.ausganslage.ausgangslageBackend.controller;

import com.ausganslage.ausgangslageBackend.exception.InvalidCredentialsException;
import com.ausganslage.ausgangslageBackend.exception.UserNotFoundException;
import com.ausganslage.ausgangslageBackend.model.UserAccount;
import com.ausganslage.ausgangslageBackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            if (request == null || request.username() == null || request.password() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Username and password required"));
            }

            UserAccount user = userService.authenticate(request.username(), request.password());
            return ResponseEntity.ok(new LoginResponse(user.getId(), user.getUsername(), "Login successful"));

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "errorCode", e.getErrorCode()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "errorCode", e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Authentication error: " + e.getMessage()));
        }
    }

    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(Long userId, String username, String message) {
    }
}
