package kz.gov.drivingexam.dto.response;

import kz.gov.drivingexam.enums.ExamType;
import kz.gov.drivingexam.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationResponse {
    private Long id;
    private Long studentId;
    private String studentFullName;
    private ExamType examType;
    private LocalDateTime examDateTime;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}