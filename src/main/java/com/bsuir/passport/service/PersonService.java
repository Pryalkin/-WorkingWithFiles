package com.bsuir.passport.service;

import com.bsuir.passport.dto.PersonDTO;
import com.bsuir.passport.dto.answer.PersonAnswerDTO;
import com.bsuir.passport.exception.model.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface PersonService {
    void registration(String name, String surname, String patronymic, String passportSeries, String passportNumber, String dateOfBirth, MultipartFile file, String usernameWithToken) throws UsernameExistException, ParseException, IOException;

    List<PersonAnswerDTO> getPeople();

    void addManager(Long id);

    void deleteManager(Long id);
}
