package com.bsuir.passport.service;

import com.bsuir.passport.dto.answer.SampleApplicationDTO;
import com.bsuir.passport.model.SampleApplication;

import java.util.List;

public interface SampleApplicationService {
    void create(SampleApplication sa);

    List<SampleApplicationDTO> getAll();

}
