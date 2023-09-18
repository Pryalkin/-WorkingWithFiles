package com.bsuir.passport.controller;

import com.bsuir.passport.constant.HttpAnswer;
import com.bsuir.passport.dto.LoginUserDTO;
import com.bsuir.passport.dto.answer.LoginUserAnswerDTO;
import com.bsuir.passport.dto.util.HttpResponse;
import com.bsuir.passport.exception.ExceptionHandling;
import com.bsuir.passport.exception.model.PasswordException;
import com.bsuir.passport.exception.model.UsernameExistException;
import com.bsuir.passport.model.user.User;
import com.bsuir.passport.model.user.UserPrincipal;
import com.bsuir.passport.service.UserService;
import com.bsuir.passport.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bsuir.passport.constant.HttpAnswer.USER_SUCCESSFULLY_REGISTERED;
import static com.bsuir.passport.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController extends ExceptionHandling {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestBody LoginUserDTO loginUserDTO) throws UsernameExistException, PasswordException {
        userService.registration(loginUserDTO);
        return HttpAnswer.response(CREATED, USER_SUCCESSFULLY_REGISTERED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserAnswerDTO> login(@RequestBody LoginUserDTO loginUserDTO) throws UsernameExistException {
        authenticate(loginUserDTO.getUsername(), loginUserDTO.getPassword());
        User loginUser = userService.findByUsername(loginUserDTO.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        LoginUserAnswerDTO loginUserAnswerDTO = new LoginUserAnswerDTO();
        loginUserAnswerDTO.setUsername(loginUser.getUsername());
        loginUserAnswerDTO.setRole(loginUser.getRole());
        loginUserAnswerDTO.setAuthorities(loginUser.getAuthorities());
        return new ResponseEntity<>(loginUserAnswerDTO, jwtHeader, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

}