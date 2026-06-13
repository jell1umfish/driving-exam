package kz.gov.drivingexam.service;

import kz.gov.drivingexam.dto.request.CreateStudentRequest;
import kz.gov.drivingexam.dto.response.StudentResponse;
import kz.gov.drivingexam.entity.Student;
import kz.gov.drivingexam.exception.BusinessException;
import kz.gov.drivingexam.exception.ResourceNotFoundException;
import kz.gov.drivingexam.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void create_success() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setIin("730128302202");
        request.setFirstName("Али");
        request.setLastName("Иванов");
        request.setPhone("+77771111111");

        Student student = Student.builder()
                .id(1L)
                .iin("730128302202")
                .firstName("Али")
                .lastName("Иванов")
                .phone("+77771111111")
                .build();

        when(studentRepository.existsByIin(request.getIin())).thenReturn(false);
        when(studentRepository.save(any())).thenReturn(student);

        StudentResponse response = studentService.create(request);

        assertNotNull(response);
        assertEquals("730128302202", response.getIin());
        assertEquals("Али", response.getFirstName());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void create_duplicateIin_throwsException() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setIin("730128302202");
        request.setFirstName("Али");
        request.setLastName("Иванов");
        request.setPhone("+77771111111");

        when(studentRepository.existsByIin(request.getIin())).thenReturn(true);

        assertThrows(BusinessException.class, () -> studentService.create(request));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getById_notFound_throwsException() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getById(999L));
    }

    @Test
    void getById_success() {
        Student student = Student.builder()
                .id(1L)
                .iin("730128302202")
                .firstName("Али")
                .lastName("Иванов")
                .phone("+77771111111")
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Али", response.getFirstName());
    }
}