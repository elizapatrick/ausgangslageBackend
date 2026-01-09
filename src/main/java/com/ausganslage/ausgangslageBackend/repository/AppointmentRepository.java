package com.ausganslage.ausgangslageBackend.repository;

import com.ausganslage.ausgangslageBackend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByUserIdAndFromDate(Long userId, LocalDate fromDate);

    List<Appointment> findByUserIdOrderByFromDateAscFromTimeAsc(Long userId);

    int deleteByUserIdAndFromDate(Long userId, LocalDate fromDate);
}
