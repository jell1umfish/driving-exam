package kz.gov.drivingexam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateStudentRequest {

    @NotBlank(message = "ИИН обязателен")
    @ValidIin
    @Pattern(regexp = "\\d{12}", message = "ИИН должен содержать ровно 12 цифр")
    private String iin;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    private String phone;
}