package com.bsuir.passport.repository;

import com.bsuir.passport.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<List<Application>> findByPersonUserUsername(String username);
}
