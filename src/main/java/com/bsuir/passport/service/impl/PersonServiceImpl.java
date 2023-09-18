package com.bsuir.passport.service.impl;

import com.bsuir.passport.dto.answer.PersonAnswerDTO;
import com.bsuir.passport.enumeration.Role;
import com.bsuir.passport.exception.model.UsernameExistException;
import com.bsuir.passport.model.Person;
import com.bsuir.passport.model.user.User;
import com.bsuir.passport.repository.PersonRepository;
import com.bsuir.passport.service.PersonService;
import com.bsuir.passport.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.bsuir.passport.constant.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void registration(String name, String surname, String patronymic, String passportSeries, String passportNumber, String dateOfBirth, MultipartFile file, String usernameWithToken) throws UsernameExistException, ParseException, IOException {
        User user = userService.findByUsername(usernameWithToken);
        user.setRole(Role.ROLE_PERSON.name());
        user.setAuthorities(Role.ROLE_PERSON.getAuthorities());
        Person person = savePhoto(usernameWithToken, file);
        person.setName(name);
        person.setSurname(surname);
        person.setPatronymic(patronymic);
        person.setPassportSeries(passportSeries);
        person.setPassportNumber(passportNumber);
        val formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("Europe/Minsk"));
        val db = formatter.parse(dateOfBirth);
        person.setDateOfBirth(db);
        person.setUser(user);
        personRepository.save(person);
    }

    @Override
    public List<PersonAnswerDTO> getPeople() {
        List<Person> people = personRepository.findAll();
        List<PersonAnswerDTO> personDTO = people.stream().map(p -> {
            PersonAnswerDTO pADTO = new PersonAnswerDTO();
            pADTO.setId(p.getId());
            pADTO.setName(p.getName());
            pADTO.setSurname(p.getSurname());
            pADTO.setPatronymic(p.getPatronymic());
            pADTO.setDateOfBirth(p.getDateOfBirth());
            pADTO.setPassportSeries(p.getPassportSeries());
            pADTO.setPassportNumber(p.getPassportNumber());
            pADTO.setRole(p.getUser().getRole());
            return pADTO;
        }).toList();
        return personDTO;
    }

    @Override
    public void addManager(Long id) {
        Person person = personRepository.getReferenceById(id);
        person.getUser().setRole(Role.ROLE_MANAGER.name());
        person.getUser().setAuthorities(Role.ROLE_MANAGER.getAuthorities());
        personRepository.save(person);
    }

    @Override
    public void deleteManager(Long id) {
        Person person = personRepository.getReferenceById(id);
        person.getUser().setRole(Role.ROLE_PERSON.name());
        person.getUser().setAuthorities(Role.ROLE_PERSON.getAuthorities());
        personRepository.save(person);
    }

    private Person savePhoto(String username, MultipartFile photo) throws IOException {
        if (photo != null){
            Path userFolder = Paths.get(USER_FOLDER + FORWARD_SLASH + username).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)){
                Files.createDirectories(userFolder);
            }
            String name = generateName();
            Files.deleteIfExists(Paths.get(userFolder + FORWARD_SLASH + name +  DOT + JPG_EXTENSION));
            Files.copy(photo.getInputStream(), userFolder.resolve(name + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            Person person = new Person();
            person.setImagePassport(setLogoImageUrl(username, name));
            return person;
        }
        return new Person();
    }

    private String setLogoImageUrl(String username, String name) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path(USER_PATH + FORWARD_SLASH + username + FORWARD_SLASH + name + DOT + JPG_EXTENSION).toUriString();
    }

    private String generateName() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}