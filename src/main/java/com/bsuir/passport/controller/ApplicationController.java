package com.bsuir.passport.controller;

import com.bsuir.passport.constant.HttpAnswer;
import com.bsuir.passport.dto.answer.ApplicationAnswerDTO;
import com.bsuir.passport.dto.util.HttpResponse;
import com.bsuir.passport.service.ApplicationService;
import com.bsuir.passport.utility.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.bsuir.passport.constant.FileConstant.FORWARD_SLASH;
import static com.bsuir.passport.constant.FileConstant.USER_FOLDER;
import static com.bsuir.passport.constant.HttpAnswer.USER_SUCCESSFULLY_REGISTERED;
import static com.bsuir.passport.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/application")
@AllArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestParam Long id,
                                                     HttpServletRequest request) throws IOException {
        String usernameWithToken = getUsernameWithToken(request);
        applicationService.registration(id, usernameWithToken);
        return HttpAnswer.response(CREATED, USER_SUCCESSFULLY_REGISTERED);
    }

    @GetMapping("/get/inactivity")
    public ResponseEntity<List<ApplicationAnswerDTO>> getInactivity(HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.getInactivity(usernameWithToken);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/get/activity")
    public ResponseEntity<List<ApplicationAnswerDTO>> getActivity(HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.getActivity(usernameWithToken);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/get/ready")
    public ResponseEntity<List<ApplicationAnswerDTO>> getReady(HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.getReady(usernameWithToken);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/get/payment")
    public ResponseEntity<List<ApplicationAnswerDTO>> getPayment(HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.getPayment(usernameWithToken);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/accept")
    public ResponseEntity<List<ApplicationAnswerDTO>> accept(@RequestParam Long id,
                                                             HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.accept(usernameWithToken, id);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<List<ApplicationAnswerDTO>> ready(@RequestParam Long id,
                                                             HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.ready(usernameWithToken, id);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping("/payment")
    public ResponseEntity<List<ApplicationAnswerDTO>> payment(@RequestParam Long id,
                                                             HttpServletRequest request){
        String usernameWithToken = getUsernameWithToken(request);
        List<ApplicationAnswerDTO> aa = applicationService.payment(usernameWithToken, id);
        return new ResponseEntity<>(aa, OK);
    }

    @GetMapping(path = "/{username}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("username") String username,
                                               @PathVariable("fileName") String fileName,
                                               HttpServletResponse response) throws IOException {
        // Проверяем, есть ли файл
        Path file = Paths.get(USER_FOLDER + FORWARD_SLASH + username + FORWARD_SLASH + fileName);
        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        // Устанавливаем заголовки для ответа
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Читаем байты файла и отправляем в браузер
        byte[] data = Files.readAllBytes(file);
        return ResponseEntity.ok().body(data);
    }

    private String getUsernameWithToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        return jwtTokenProvider.getSubject(token);
    }
}