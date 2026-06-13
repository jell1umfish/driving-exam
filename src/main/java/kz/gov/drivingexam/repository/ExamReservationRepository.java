package kz.gov.drivingexam.repository;

import kz.gov.drivingexam.entity.ExamReservation;
import kz.gov.drivingexam.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kz.gov.drivingexam.enums.ExamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamReservationRepository extends JpaRepository<ExamReservation, Long> {
    Optional<ExamReservation> findByStudentIdAndStatus(Long studentId, ReservationStatus status);
    boolean existsByStudentIdAndStatus(Long studentId, ReservationStatus status);
    boolean existsByStudentIdAndExamTypeAndStatus(Long studentId, ExamType examType, ReservationStatus status);
    Page<ExamReservation> findByStatus(ReservationStatus status, Pageable pageable);
    Page<ExamReservation> findByExamType(ExamType examType, Pageable pageable);
    Page<ExamReservation> findByStudentId(Long studentId, Pageable pageable);
    Page<ExamReservation> findByStudentIdAndExamType(Long studentId, ExamType examType, Pageable pageable);
    Page<ExamReservation> findByStudentIdAndStatus(Long studentId, ReservationStatus status, Pageable pageable);
    List<ExamReservation> findByStatusAndExamDateTimeBefore(ReservationStatus status, LocalDateTime dateTime);
}
