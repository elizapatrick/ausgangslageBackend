package com.ausganslage.ausgangslageBackend.controller;

import com.ausganslage.ausgangslageBackend.exception.InvalidAppointmentDataException;
import com.ausganslage.ausgangslageBackend.exception.UserNotFoundException;
import com.ausganslage.ausgangslageBackend.model.Appointment;
import com.ausganslage.ausgangslageBackend.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Appointment controller with proper exception handling
 * Handles CRUD operations for appointments
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Create new appointment
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment,
            @RequestParam Long userId) {
        try {
            if (appointment == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Appointment data required"));
            }
            Appointment saved = appointmentService.createAppointment(appointment, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (InvalidAppointmentDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage(), "errorCode", e.getErrorCode()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "errorCode", e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating appointment: " + e.getMessage()));
        }
    }

    /**
     * Get all appointments for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAppointments(@PathVariable Long userId) {
        try {
            List<Appointment> appointments = appointmentService.getUserAppointments(userId);
            return ResponseEntity.ok(appointments);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "errorCode", e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error retrieving appointments: " + e.getMessage()));
        }
    }

    /**
     * Get appointments for specific date
     */
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<?> getUserAppointmentsByDate(@PathVariable Long userId,
            @PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Appointment> appointments = appointmentService.getUserAppointmentsByDate(userId, localDate);
            return ResponseEntity.ok(appointments);

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid date format: " + e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage(), "errorCode", e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error retrieving appointments: " + e.getMessage()));
        }
    }

    /**
     * Update appointment notes
     */
    @PutMapping("/{appointmentId}/notes")
    public ResponseEntity<?> updateNotes(@PathVariable Long appointmentId,
            @RequestBody Map<String, String> request) {
        try {
            String notes = request.get("notes");
            Appointment updated = appointmentService.updateAppointmentNotes(appointmentId, notes);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Appointment not found"));
            }
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating appointment: " + e.getMessage()));
        }
    }

    /**
     * Delete appointment
     */
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok(Map.of("message", "Appointment deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting appointment: " + e.getMessage()));
        }
    }
}
