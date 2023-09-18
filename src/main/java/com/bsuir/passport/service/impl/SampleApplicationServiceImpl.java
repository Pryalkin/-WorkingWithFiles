package com.bsuir.passport.service.impl;

import com.bsuir.passport.dto.answer.SampleApplicationDTO;
import com.bsuir.passport.model.SampleApplication;
import com.bsuir.passport.repository.SampleApplicationRepository;
import com.bsuir.passport.service.SampleApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bsuir.passport.constant.FileConstant.*;

@Service
@AllArgsConstructor
public class SampleApplicationServiceImpl implements SampleApplicationService {

    private final SampleApplicationRepository sampleApplicationRepository;


    @Override
    public void create(SampleApplication sa) {
        sampleApplicationRepository.save(sa);
    }

    @Override
    public List<SampleApplicationDTO> getAll() {
        return sampleApplicationRepository.findAll().stream().map(sa -> {
            SampleApplicationDTO saDTO = new SampleApplicationDTO();
            saDTO.setId(sa.getId());
            saDTO.setName(sa.getName());
            saDTO.setText(sa.getText());
            saDTO.setUrl(sa.getUrl());
            return saDTO;
        }).collect(Collectors.toList());
    }

}
