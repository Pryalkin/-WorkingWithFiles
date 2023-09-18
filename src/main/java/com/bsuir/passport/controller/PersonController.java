package com.bsuir.passport.controller;

import com.bsuir.passport.constant.HttpAnswer;
import com.bsuir.passport.dto.answer.PersonAnswerDTO;
import com.bsuir.passport.dto.util.HttpResponse;
import com.bsuir.passport.exception.model.PasswordException;
import com.bsuir.passport.exception.model.UsernameExistException;
import com.bsuir.passport.service.PersonService;
import com.bsuir.passport.utility.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.bsuir.passport.constant.HttpAnswer.*;
import static com.bsuir.passport.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestParam String name,
                                                     @RequestParam String surname,
                                                     @RequestParam String patronymic,
                                                     @RequestParam String passportSeries,
                                                     @RequestParam String passportNumber,
                                                     @RequestParam String dateOfBirth,
                                                     @RequestParam(value = "file") MultipartFile file,
                                                     HttpServletRequest request) throws UsernameExistException, PasswordException, ParseException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        String usernameWithToken = jwtTokenProvider.getSubject(token);
        personService.registration(name, surname, patronymic, passportSeries, passportNumber, dateOfBirth, file, usernameWithToken);
        return HttpAnswer.response(CREATED, USER_SUCCESSFULLY_REGISTERED);
    }

    @GetMapping("/get/people")
    public ResponseEntity<List<PersonAnswerDTO>> getPeople(){
        List<PersonAnswerDTO> people = personService.getPeople();
        return new ResponseEntity<>(people, OK);
    }

    @PostMapping("/add/manager")
    public ResponseEntity<HttpResponse> addManager(@RequestParam Long id){
        personService.addManager(id);
        return HttpAnswer.response(CREATED, MANAGER_SUCCESSFULLY_REGISTERED);
    }

    @PostMapping("/delete/manager")
    public ResponseEntity<HttpResponse> deleteManager(@RequestParam Long id){
        personService.deleteManager(id);
        return HttpAnswer.response(CREATED, MANAGER_SUCCESSFULLY_UNREGISTERED);
    }

}