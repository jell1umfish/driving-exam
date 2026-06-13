package kz.gov.drivingexam.dto.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IinValidator implements ConstraintValidator<ValidIin, String> {

    @Override
    public boolean isValid(String iin, ConstraintValidatorContext context) {
        if (iin == null || !iin.matches("\\d{12}")) return false;

        // Контрольный разряд — весовые коэффициенты
        int[] weights1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] weights2 = {3, 4, 5, 6, 7, 8, 9, 10, 11, 1, 2};

        int sum = 0;
        for (int i = 0; i < 11; i++) {
            sum += Character.getNumericValue(iin.charAt(i)) * weights1[i];
        }

        int control = sum % 11;

        if (control == 10) {
            sum = 0;
            for (int i = 0; i < 11; i++) {
                sum += Character.getNumericValue(iin.charAt(i)) * weights2[i];
            }
            control = sum % 11;
        }

        return control == Character.getNumericValue(iin.charAt(11));
    }
}