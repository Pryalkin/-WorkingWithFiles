package com.bsuir.passport.dto.answer;

import lombok.Data;

import java.util.Date;

@Data
public class PersonAnswerDTO {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private Date dateOfBirth;
    private String passportSeries;
    private String passportNumber;
    private String role;
}
