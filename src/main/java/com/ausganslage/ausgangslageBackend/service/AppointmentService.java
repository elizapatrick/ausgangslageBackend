package com.ausganslage.ausgangslageBackend.service;

import com.ausganslage.ausgangslageBackend.exception.AppointmentNotFoundException;
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
     * @throws UserNotFoundException if user doesn't exist
     */
    public Appointment createAppointment(Appointment appointment, Long userId)
            throws UserNotFoundException {

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
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
     * 
     * @throws AppointmentNotFoundException if appointment doesn't exist
     */
    public Appointment getAppointmentById(Long appointmentId)
            throws AppointmentNotFoundException {

        try {
            return appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + appointmentId));
        } catch (AppointmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentNotFoundException("Error retrieving appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Update appointment notes
     * 
     * @throws AppointmentNotFoundException if appointment doesn't exist
     */
    public Appointment updateAppointmentNotes(Long appointmentId, String notes)
            throws AppointmentNotFoundException {

        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + appointmentId));

            appointment.setNotes(notes);
            return appointmentRepository.save(appointment);
        } catch (AppointmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentNotFoundException("Error updating appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Delete appointment
     * 
     * @throws AppointmentNotFoundException if appointment doesn't exist
     */
    public void deleteAppointment(Long appointmentId)
            throws AppointmentNotFoundException {

        try {
            if (!appointmentRepository.existsById(appointmentId)) {
                throw new AppointmentNotFoundException("Appointment not found: " + appointmentId);
            }
            appointmentRepository.deleteById(appointmentId);
        } catch (AppointmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AppointmentNotFoundException("Error deleting appointment: " + e.getMessage(), e);
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
