package kz.gov.drivingexam.service;

import kz.gov.drivingexam.dto.request.CreateStudentRequest;
import kz.gov.drivingexam.dto.request.UpdateStudentRequest;
import kz.gov.drivingexam.dto.response.StudentResponse;
import kz.gov.drivingexam.entity.Student;
import kz.gov.drivingexam.exception.BusinessException;
import kz.gov.drivingexam.exception.ResourceNotFoundException;
import kz.gov.drivingexam.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse create(CreateStudentRequest request) {
        if (studentRepository.existsByIin(request.getIin())) {
            throw new BusinessException("Студент с ИИН " + request.getIin() + " уже существует");
        }

        Student student = Student.builder()
                .iin(request.getIin())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .build();

        return toResponse(studentRepository.save(student));
    }

    public StudentResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public Page<StudentResponse> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(this::toResponse);
    }

    public StudentResponse update(Long id, UpdateStudentRequest request) {
        Student student = findById(id);

        if (request.getIin() != null) {
            if (!request.getIin().equals(student.getIin()) &&
                    studentRepository.existsByIin(request.getIin())) {
                throw new BusinessException("Студент с ИИН " + request.getIin() + " уже существует");
            }
            student.setIin(request.getIin());
        }
        if (request.getFirstName() != null) student.setFirstName(request.getFirstName());
        if (request.getLastName() != null) student.setLastName(request.getLastName());
        if (request.getPhone() != null) student.setPhone(request.getPhone());

        return toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        findById(id);
        studentRepository.deleteById(id);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с ID " + id + " не найден"));
    }

    private StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .iin(student.getIin())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .phone(student.getPhone())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}