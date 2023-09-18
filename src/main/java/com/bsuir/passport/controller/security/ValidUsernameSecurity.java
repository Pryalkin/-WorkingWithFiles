package com.bsuir.passport.controller.security;

import com.bsuir.passport.exception.model.NoRightException;
import com.bsuir.passport.utility.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import static com.bsuir.passport.constant.ExceptionConstant.THE_REQUEST_DOES_NOT_MATCH_YOUR_RIGHTS;
import static com.bsuir.passport.constant.SecurityConstant.TOKEN_PREFIX;


public class ValidUsernameSecurity {

    public static void checkUsernameForValidity(HttpServletRequest request, JWTTokenProvider jwtTokenProvider, String username) throws NoRightException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        String usernameWithToken = jwtTokenProvider.getSubject(token);
        if (!usernameWithToken.equals(username)) throw new NoRightException(THE_REQUEST_DOES_NOT_MATCH_YOUR_RIGHTS);
    }
}