package com.bsuir.passport.service;

import com.bsuir.passport.dto.LoginUserDTO;
import com.bsuir.passport.exception.model.PasswordException;
import com.bsuir.passport.exception.model.UsernameExistException;
import com.bsuir.passport.model.user.User;

public interface UserService {

    User findByUsername(String username) throws UsernameExistException;

    void registration(LoginUserDTO loginUserDTO) throws UsernameExistException, PasswordException;
}
