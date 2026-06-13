package kz.gov.drivingexam.service;

import kz.gov.drivingexam.dto.request.CreateReservationRequest;
import kz.gov.drivingexam.dto.response.ReservationResponse;
import kz.gov.drivingexam.entity.ExamReservation;
import kz.gov.drivingexam.entity.Student;
import kz.gov.drivingexam.enums.ExamType;
import kz.gov.drivingexam.enums.ReservationStatus;
import kz.gov.drivingexam.exception.BusinessException;
import kz.gov.drivingexam.exception.ResourceNotFoundException;
import kz.gov.drivingexam.repository.ExamReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamReservationService {

    private final ExamReservationRepository reservationRepository;
    private final StudentService studentService;

    public ReservationResponse create(CreateReservationRequest request) {
        Student student = studentService.findById(request.getStudentId());

        if (reservationRepository.existsByStudentIdAndStatus(
                request.getStudentId(), ReservationStatus.ACTIVE)) {
            throw new BusinessException("У студента уже есть активное бронирование");
        }
        if (request.getExamType() == ExamType.PRACTICE) {
            if (!reservationRepository.existsByStudentIdAndExamTypeAndStatus(
                    request.getStudentId(), ExamType.THEORY, ReservationStatus.COMPLETED)) {
                throw new BusinessException("Нельзя записаться на практику без сданного теоретического экзамена");
            }
        }
        ExamReservation reservation = ExamReservation.builder()
                .student(student)
                .examType(request.getExamType())
                .examDateTime(request.getExamDateTime())
                .status(ReservationStatus.ACTIVE)
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public Page<ReservationResponse> getAll(ReservationStatus status, ExamType examType, Long studentId, Pageable pageable) {
        if (studentId != null && examType != null) {
            return reservationRepository.findByStudentIdAndExamType(studentId, examType, pageable)
                    .map(this::toResponse);
        }
        if (studentId != null && status != null) {
            return reservationRepository.findByStudentIdAndStatus(studentId, status, pageable)
                    .map(this::toResponse);
        }
        if (studentId != null) {
            return reservationRepository.findByStudentId(studentId, pageable).map(this::toResponse);
        }
        if (examType != null) {
            return reservationRepository.findByExamType(examType, pageable).map(this::toResponse);
        }
        if (status != null) {
            return reservationRepository.findByStatus(status, pageable).map(this::toResponse);
        }
        return reservationRepository.findAll(pageable).map(this::toResponse);
    }

    public ReservationResponse update(Long id, CreateReservationRequest request) {
        ExamReservation reservation = findById(id);

        reservation.setExamType(request.getExamType());
        reservation.setExamDateTime(request.getExamDateTime());

        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse complete(Long id) {
        ExamReservation reservation = findById(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException("Нельзя завершить отменённое бронирование");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new BusinessException("Бронирование уже завершено");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        return toResponse(reservationRepository.save(reservation));
    }

    public void delete(Long id) {
        findById(id);
        reservationRepository.deleteById(id);
    }

    public ReservationResponse cancel(Long id) {
        ExamReservation reservation = findById(id);
        if (reservation.getStatus() == ReservationStatus.EXPIRED) {
            throw new BusinessException("Нельзя отменить истёкшее бронирование");
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new BusinessException("Нельзя отменить завершённое бронирование");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException("Бронирование уже отменено");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return toResponse(reservationRepository.save(reservation));
    }

    private ExamReservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Бронирование с ID " + id + " не найдено"));
    }

    private ReservationResponse toResponse(ExamReservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .studentId(reservation.getStudent().getId())
                .studentFullName(reservation.getStudent().getFirstName()
                        + " " + reservation.getStudent().getLastName())
                .examType(reservation.getExamType())
                .examDateTime(reservation.getExamDateTime())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}