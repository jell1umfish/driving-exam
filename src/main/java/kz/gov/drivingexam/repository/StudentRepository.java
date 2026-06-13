package kz.gov.drivingexam.repository;

import kz.gov.drivingexam.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByIin(String iin);
    boolean existsByIin(String iin);
    Page<Student> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    Page<Student> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}