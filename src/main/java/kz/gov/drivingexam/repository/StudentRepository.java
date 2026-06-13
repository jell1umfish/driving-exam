package kz.gov.drivingexam.repository;

import kz.gov.drivingexam.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByIin(String iin);
    boolean existsByIin(String iin);
}