package com.bsuir.passport.dto.answer;

import com.bsuir.passport.dto.PersonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class ApplicationAnswerDTO {
    private Long id;
    private String number;
    private PersonDTO personDTO;
    private String file;
    private String status;
    private Date date;
    private SampleApplicationDTO sampleApplicationDTO;
}
