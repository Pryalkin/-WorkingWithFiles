package com.bsuir.passport.service.impl;

import com.bsuir.passport.dto.PersonDTO;
import com.bsuir.passport.dto.answer.ApplicationAnswerDTO;
import com.bsuir.passport.dto.answer.SampleApplicationDTO;
import com.bsuir.passport.enumeration.Role;
import com.bsuir.passport.enumeration.Status;
import com.bsuir.passport.model.Application;
import com.bsuir.passport.model.Person;
import com.bsuir.passport.model.SampleApplication;
import com.bsuir.passport.repository.ApplicationRepository;
import com.bsuir.passport.repository.PersonRepository;
import com.bsuir.passport.repository.SampleApplicationRepository;
import com.bsuir.passport.service.ApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.bsuir.passport.constant.FileConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PersonRepository personRepository;
    private final SampleApplicationRepository sampleApplicationRepository;

    @Override
    @Transactional
    public void registration(Long id, String usernameWithToken) throws IOException {
        Person person = personRepository.findByUserUsername(usernameWithToken).get();
        SampleApplication sa = sampleApplicationRepository.findById(id).get();
        String name = generateName();
        Path file = Paths.get(USER_FOLDER + FORWARD_SLASH + sa.getName() + DOT + DOCX_EXTENSION);
        try (FileInputStream fileInputStream = new FileInputStream(file.toAbsolutePath().toFile())) {
            XWPFDocument doc = new XWPFDocument(fileInputStream);
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph para : cell.getParagraphs()) {
                            for (XWPFRun run : para.getRuns()) {
                                CTText ctText = run.getCTR().getTArray(0);
                                if (ctText.getStringValue().equals("Иван")) {
                                    String replacedText = ctText.getStringValue().replace("Иван", person.getName());
                                    ctText.setStringValue(replacedText);
                                }
                                if (ctText.getStringValue().equals("Иванов")) {
                                    String replacedText = ctText.getStringValue().replace("Иванов", person.getSurname());
                                    ctText.setStringValue(replacedText);
                                }
                                if (ctText.getStringValue().equals("Иванович")) {
                                    String replacedText = ctText.getStringValue().replace("Иванович", person.getPatronymic());
                                    ctText.setStringValue(replacedText);
                                }
                                if (ctText.getStringValue().equals("01.01.2000")) {
                                    String replacedText = ctText.getStringValue().replace("01.01.2000", person.getDateOfBirth().toString());
                                    ctText.setStringValue(replacedText);
                                }
                                if (ctText.getStringValue().equals("КВ")) {
                                    String replacedText = ctText.getStringValue().replace("КВ", person.getPassportSeries());
                                    ctText.setStringValue(replacedText);
                                }
                                if (ctText.getStringValue().equals("1111111")) {
                                    String replacedText = ctText.getStringValue().replace("1111111", person.getPassportNumber());
                                    ctText.setStringValue(replacedText);
                                }
                            }
                        }
                    }
                }
            }
            Path userFolder = Paths.get(USER_FOLDER + FORWARD_SLASH + person.getUser().getUsername() + FORWARD_SLASH + sa.getName()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)){
                Files.createDirectories(userFolder);
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(userFolder + FORWARD_SLASH + name + DOT + DOCX_EXTENSION)) {
                doc.write(fileOutputStream);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Application application = new Application();
        application.setNumber(generateName());
        application.setDate(new Date());
        application.setFile(setLogoDOCXUrl(person.getUser().getUsername(), name));
        application.setStatus(Status.INACTIVITY.name());
        application.setSampleApplication(sa);
        person.addApp(application);
        personRepository.save(person);
    }

    @Override
    public List<ApplicationAnswerDTO> getInactivity(String usernameWithToken) {
        Person person = personRepository.findByUserUsername(usernameWithToken).get();
        List<Application> applications = null;
        if (person.getUser().getRole().equals(Role.ROLE_PERSON.name())){
            applications = person.getApplications().stream()
                    .filter(app -> app.getStatus().equals(Status.INACTIVITY.name())).toList();
        } else {
            applications = applicationRepository.findAll().stream()
                    .filter(app -> app.getStatus().equals(Status.INACTIVITY.name())).toList();
        }
        return applications.stream().map(this::createApplicationAnswerDTO).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationAnswerDTO> getActivity(String usernameWithToken) {
        Person person = personRepository.findByUserUsername(usernameWithToken).get();
        List<Application> applications = null;
        if (person.getUser().getRole().equals(Role.ROLE_PERSON.name())){
            applications = applicationRepository.findByPersonUserUsername(usernameWithToken)
                    .get().stream().filter(app -> app.getStatus().equals(Status.ACTIVITY.name())).toList();
        } else {
            applications = applicationRepository.findAll().stream()
                    .filter(app -> app.getStatus().equals(Status.ACTIVITY.name())).toList();
        }
        return applications.stream().map(this::createApplicationAnswerDTO).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationAnswerDTO> getReady(String usernameWithToken) {
        Person person = personRepository.findByUserUsername(usernameWithToken).get();
        List<Application> applications = null;
        if (person.getUser().getRole().equals(Role.ROLE_PERSON.name())){
            applications = applicationRepository.findByPersonUserUsername(usernameWithToken)
                    .get().stream().filter(app -> app.getStatus().equals(Status.READY.name())).toList();
        } else {
            applications = applicationRepository.findAll().stream()
                    .filter(app -> app.getStatus().equals(Status.READY.name())).toList();
        }
        return applications.stream().map(this::createApplicationAnswerDTO).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationAnswerDTO> getPayment(String usernameWithToken) {
        Person person = personRepository.findByUserUsername(usernameWithToken).get();
        List<Application> applications = null;
        if (person.getUser().getRole().equals(Role.ROLE_PERSON.name())){
           applications = applicationRepository.findByPersonUserUsername(usernameWithToken)
                    .get().stream().filter(app -> app.getStatus().equals(Status.PAYMENT.name())).toList();
        } else {
            applications = applicationRepository.findAll().stream()
                    .filter(app -> app.getStatus().equals(Status.PAYMENT.name())).toList();
        }
        return applications.stream().map(this::createApplicationAnswerDTO).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationAnswerDTO> accept(String usernameWithToken, Long id) {
        Application application = applicationRepository.getReferenceById(id);
        application.setStatus(Status.PAYMENT.name());
        applicationRepository.save(application);
        return getInactivity(usernameWithToken);
    }

    @Override
    public List<ApplicationAnswerDTO> payment(String usernameWithToken, Long id) {
        Application application = applicationRepository.getReferenceById(id);
        application.setStatus(Status.ACTIVITY.name());
        applicationRepository.save(application);
        return getPayment(usernameWithToken);
    }

    @Override
    public List<ApplicationAnswerDTO> ready(String usernameWithToken, Long id) {
        Application application = applicationRepository.getReferenceById(id);
        application.setStatus(Status.READY.name());
        applicationRepository.save(application);
        return getActivity(usernameWithToken);
    }

    private ApplicationAnswerDTO createApplicationAnswerDTO(Application application) {
        ApplicationAnswerDTO aaDTO = new ApplicationAnswerDTO();
        aaDTO.setId(application.getId());
        aaDTO.setStatus(application.getStatus());
        aaDTO.setNumber(application.getNumber());
        aaDTO.setFile(application.getFile());
        aaDTO.setDate(application.getDate());
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(application.getPerson().getName());
        personDTO.setSurname(application.getPerson().getSurname());
        personDTO.setPatronymic(application.getPerson().getPatronymic());
        personDTO.setDateOfBirth(application.getPerson().getDateOfBirth());
        personDTO.setPassportSeries(application.getPerson().getPassportSeries());
        personDTO.setPassportNumber(application.getPerson().getPassportNumber());
        aaDTO.setPersonDTO(personDTO);
        SampleApplicationDTO saDTO = new SampleApplicationDTO();
        saDTO.setId(application.getSampleApplication().getId());
        saDTO.setName(application.getSampleApplication().getName());
        saDTO.setText(application.getSampleApplication().getText());
        saDTO.setUrl(application.getSampleApplication().getUrl());
        aaDTO.setSampleApplicationDTO(saDTO);
        return aaDTO;
    }

    private String generateName() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String setLogoDOCXUrl(String username, String name) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path(APP_PATH + FORWARD_SLASH + username + FORWARD_SLASH + name + DOT + DOCX_EXTENSION).toUriString();
    }

}
