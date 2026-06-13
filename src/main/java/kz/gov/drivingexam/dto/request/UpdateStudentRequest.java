package kz.gov.drivingexam.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateStudentRequest {

    @Pattern(regexp = "\\d{12}", message = "ИИН должен содержать ровно 12 цифр")
    @ValidIin
    private String iin;
    private String firstName;
    private String lastName;
    private String phone;
}