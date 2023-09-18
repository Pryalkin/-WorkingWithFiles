package com.bsuir.passport.service.impl;

import com.bsuir.passport.dto.LoginUserDTO;
import com.bsuir.passport.exception.model.PasswordException;
import com.bsuir.passport.exception.model.UsernameExistException;
import com.bsuir.passport.model.user.User;
import com.bsuir.passport.model.user.UserPrincipal;
import com.bsuir.passport.repository.UserRepository;
import com.bsuir.passport.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.bsuir.passport.constant.UserImplConstant.*;
import static com.bsuir.passport.enumeration.Role.ROLE_ADMIN;
import static com.bsuir.passport.enumeration.Role.ROLE_USER;

@Service
@Qualifier("userDetailsService")
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        log.info(FOUND_USER_BY_USERNAME + username);
        return userPrincipal;
    }

    @Override
    public void registration(LoginUserDTO loginUserDTO) throws UsernameExistException, PasswordException {
        validateNewUsernameAndPassword(loginUserDTO);
        User user = new User();
        user.setUsername(loginUserDTO.getUsername());
        user.setPassword(encodePassword(loginUserDTO.getPassword()));
        if (userRepository.findAll().isEmpty()) {
            user.setRole(ROLE_ADMIN.name());
            user.setAuthorities(ROLE_ADMIN.getAuthorities());
        } else {
            user.setRole(ROLE_USER.name());
            user.setAuthorities(ROLE_USER.getAuthorities());
        }
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) throws UsernameExistException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameExistException(USERNAME_ALREADY_EXISTS));
    }

    private void validateNewUsernameAndPassword(LoginUserDTO loginUserDTO) throws UsernameExistException, PasswordException {
        if (userRepository.findByUsername(loginUserDTO.getUsername()).isPresent()){
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (!loginUserDTO.getPassword().equals(loginUserDTO.getPassword2())){
            throw new PasswordException(PASSWORD_IS_NOT_VALID);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

//    private PhoneRepairRequest savePhoto(String username, MultipartFile photo, PhoneRepairRequest phoneRepairRequest) throws IOException {
//        if (photo != null){
//            Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
//            if (!Files.exists(userFolder)){
//                Files.createDirectories(userFolder);
//            }
//            String name = generateName();
//            Files.deleteIfExists(Paths.get(userFolder + FORWARD_SLASH + name +  DOT + JPG_EXTENSION));
//            Files.copy(photo.getInputStream(), userFolder.resolve(name + DOT + JPG_EXTENSION), REPLACE_EXISTING);
//            Photo ph = new Photo();
//            ph.setUrlPhoto(setLogoImageUrl(username, name));
//            phoneRepairRequest.addPhoto(ph);
//            return phoneRepairRequest;
//        }
//        return phoneRepairRequest;
//    }
//
//    private String setLogoImageUrl(String username, String name) {
//        return ServletUriComponentsBuilder.fromCurrentContextPath().
//                path(USER_IMAGE_PATH + username + FORWARD_SLASH + name + DOT + JPG_EXTENSION).toUriString();
//    }

}
