package com.bsuir.passport.service;

import com.bsuir.passport.dto.answer.ApplicationAnswerDTO;

import java.io.IOException;
import java.util.List;

public interface ApplicationService {
    void registration(Long id, String username) throws IOException;

    List<ApplicationAnswerDTO> getInactivity(String usernameWithToken);

    List<ApplicationAnswerDTO> getActivity(String usernameWithToken);

    List<ApplicationAnswerDTO> getReady(String usernameWithToken);

    List<ApplicationAnswerDTO> getPayment(String usernameWithToken);

    List<ApplicationAnswerDTO> accept(String usernameWithToken, Long id);

    List<ApplicationAnswerDTO> payment(String usernameWithToken, Long id);

    List<ApplicationAnswerDTO> ready(String usernameWithToken, Long id);
}
