package com.ausganslage.ausgangslageBackend.service;

import com.ausganslage.ausgangslageBackend.exception.InvalidAppointmentDataException;
import com.ausganslage.ausgangslageBackend.exception.UserNotFoundException;
import com.ausganslage.ausgangslageBackend.model.Appointment;
import com.ausganslage.ausgangslageBackend.model.UserAccount;
import com.ausganslage.ausgangslageBackend.repository.AppointmentRepository;
import com.ausganslage.ausgangslageBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for appointment management
 * Handles CRUD operations with proper exception handling and validation
 */
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create new appointment
     * 
     * @throws UserNotFoundException           if user doesn't exist
     * @throws InvalidAppointmentDataException if required fields are missing
     */
    public Appointment createAppointment(Appointment appointment, Long userId)
            throws UserNotFoundException, InvalidAppointmentDataException {

        if (appointment == null) {
            throw new InvalidAppointmentDataException("Appointment cannot be null");
        }

        // Validate required fields
        if (appointment.getName() == null || appointment.getName().trim().isEmpty()) {
            throw new InvalidAppointmentDataException("Appointment name is required");
        }

        if (appointment.getFromDate() == null) {
            throw new InvalidAppointmentDataException("Appointment date (fromDate) is required");
        }

        if (appointment.getDescription() == null || appointment.getDescription().trim().isEmpty()) {
            throw new InvalidAppointmentDataException("Appointment description is required");
        }

        if (appointment.getGenre() == null || appointment.getGenre().trim().isEmpty()) {
            throw new InvalidAppointmentDataException("Appointment genre is required");
        }

        try {
            UserAccount user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

            appointment.setUser(user);
            return appointmentRepository.save(appointment);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Get appointments for specific user
     * 
     * @throws UserNotFoundException if user doesn't exist
     */
    public List<Appointment> getUserAppointments(Long userId)
            throws UserNotFoundException {

        try {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User not found: " + userId);
            }
            return appointmentRepository.findByUserId(userId);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving appointments: " + e.getMessage(), e);
        }
    }

    /**
     * Get appointment by ID
     */
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElse(null);
    }

    /**
     * Update appointment notes
     */
    public Appointment updateAppointmentNotes(Long appointmentId, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            appointment.setNotes(notes);
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    /**
     * Delete appointment
     */
    public void deleteAppointment(Long appointmentId) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
        }
    }

    /**
     * Get appointments for specific user on a specific date
     */
    public List<Appointment> getUserAppointmentsByDate(Long userId, LocalDate date)
            throws UserNotFoundException {

        try {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User not found: " + userId);
            }
            return appointmentRepository.findByUserIdAndFromDate(userId, date);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving appointments by date: " + e.getMessage(), e);
        }
    }
}
