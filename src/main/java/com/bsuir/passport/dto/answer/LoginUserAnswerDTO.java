package com.bsuir.passport.dto.answer;

import lombok.Data;

@Data
public class LoginUserAnswerDTO {

    private String username;
    private String role;
    private String[] authorities;

}
