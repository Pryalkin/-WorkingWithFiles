package com.bsuir.passport.repository;

import com.bsuir.passport.model.SampleApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleApplicationRepository extends JpaRepository<SampleApplication, Long> {

}
