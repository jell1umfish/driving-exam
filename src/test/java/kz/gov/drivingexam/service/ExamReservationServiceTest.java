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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamReservationServiceTest {

    @Mock
    private ExamReservationRepository reservationRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ExamReservationService reservationService;

    private Student mockStudent() {
        return Student.builder()
                .id(1L)
                .iin("730128302202")
                .firstName("Али")
                .lastName("Иванов")
                .phone("+77771111111")
                .build();
    }

    @Test
    void create_success() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStudentId(1L);
        request.setExamType(ExamType.THEORY);
        request.setExamDateTime(LocalDateTime.now().plusDays(10));

        Student student = mockStudent();

        ExamReservation reservation = ExamReservation.builder()
                .id(1L)
                .student(student)
                .examType(ExamType.THEORY)
                .examDateTime(request.getExamDateTime())
                .status(ReservationStatus.ACTIVE)
                .build();

        when(studentService.findById(1L)).thenReturn(student);
        when(reservationRepository.existsByStudentIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(false);
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationResponse response = reservationService.create(request);

        assertNotNull(response);
        assertEquals(ReservationStatus.ACTIVE, response.getStatus());
        assertEquals(ExamType.THEORY, response.getExamType());
    }

    @Test
    void create_studentNotFound_throwsException() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStudentId(999L);
        request.setExamType(ExamType.THEORY);
        request.setExamDateTime(LocalDateTime.now().plusDays(10));

        when(studentService.findById(999L)).thenThrow(new ResourceNotFoundException("Студент не найден"));

        assertThrows(ResourceNotFoundException.class, () -> reservationService.create(request));
    }

    @Test
    void create_alreadyActiveReservation_throwsException() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStudentId(1L);
        request.setExamType(ExamType.THEORY);
        request.setExamDateTime(LocalDateTime.now().plusDays(10));

        when(studentService.findById(1L)).thenReturn(mockStudent());
        when(reservationRepository.existsByStudentIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(true);

        assertThrows(BusinessException.class, () -> reservationService.create(request));
    }

    @Test
    void create_practiceWithoutTheory_throwsException() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStudentId(1L);
        request.setExamType(ExamType.PRACTICE);
        request.setExamDateTime(LocalDateTime.now().plusDays(10));

        when(studentService.findById(1L)).thenReturn(mockStudent());
        when(reservationRepository.existsByStudentIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(false);
        when(reservationRepository.existsByStudentIdAndExamTypeAndStatus(
                1L, ExamType.THEORY, ReservationStatus.COMPLETED)).thenReturn(false);

        assertThrows(BusinessException.class, () -> reservationService.create(request));
    }

    @Test
    void cancel_success() {
        ExamReservation reservation = ExamReservation.builder()
                .id(1L)
                .student(mockStudent())
                .examType(ExamType.THEORY)
                .examDateTime(LocalDateTime.now().plusDays(5))
                .status(ReservationStatus.ACTIVE)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationResponse response = reservationService.cancel(1L);

        assertEquals(ReservationStatus.CANCELLED, response.getStatus());
    }

    @Test
    void cancel_completedReservation_throwsException() {
        ExamReservation reservation = ExamReservation.builder()
                .id(1L)
                .student(mockStudent())
                .examType(ExamType.THEORY)
                .examDateTime(LocalDateTime.now().plusDays(5))
                .status(ReservationStatus.COMPLETED)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(BusinessException.class, () -> reservationService.cancel(1L));
    }

    @Test
    void complete_success() {
        ExamReservation reservation = ExamReservation.builder()
                .id(1L)
                .student(mockStudent())
                .examType(ExamType.THEORY)
                .examDateTime(LocalDateTime.now().plusDays(5))
                .status(ReservationStatus.ACTIVE)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationResponse response = reservationService.complete(1L);

        assertEquals(ReservationStatus.COMPLETED, response.getStatus());
    }
}