package com.example.lab6_q1_employee.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotNull(message = "id can not be Null")
    @Min(value = 2, message = "id has to be 2 numbers or ")
    private int id;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 4, message = "name must be 4 letters at least")
    private String name;

    @NotNull(message = "age can not be null")
    @Positive(message = "age has to be a positive number")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "should use email format name@company.com")
    private String email;

    @NotEmpty(message = "phone can not be empty")
    @Pattern(regexp = "^\\d{10}$", message = "phone number should be 10 digits")
    private String phone;

    @NotEmpty(message = "should not be empty")
    @Pattern(regexp = "supervisor|coordinator")
    private String position;


    private boolean onLeave = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate employmentYear;

    @NotNull(message = "annualLeave can not be null")
    @Positive(message = "number of annualLeave days should be positive")
    private int annualLeave;
}
