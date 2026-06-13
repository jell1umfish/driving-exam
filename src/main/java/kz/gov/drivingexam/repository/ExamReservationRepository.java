package kz.gov.drivingexam.repository;

import kz.gov.drivingexam.entity.ExamReservation;
import kz.gov.drivingexam.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamReservationRepository extends JpaRepository<ExamReservation, Long> {
    Optional<ExamReservation> findByStudentIdAndStatus(Long studentId, ReservationStatus status);
    boolean existsByStudentIdAndStatus(Long studentId, ReservationStatus status);
}