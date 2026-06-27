package com.vinay.elearning;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findBystudentEmail(String studentEmail);
}