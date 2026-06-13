package kz.gov.drivingexam.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import kz.gov.drivingexam.enums.ExamType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationRequest {

    @NotNull(message = "ID студента обязателен")
    private Long studentId;

    @NotNull(message = "Тип экзамена обязателен")
    private ExamType examType;

    @NotNull(message = "Дата и время экзамена обязательны")
    @Future(message = "Дата и время экзамена должны быть в будущем")
    private LocalDateTime examDateTime;
}