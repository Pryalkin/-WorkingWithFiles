package com.bsuir.passport.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PersonDTO {
    private String name;
    private String surname;
    private String patronymic;
    private Date dateOfBirth;
    private String passportSeries;
    private String passportNumber;
}
