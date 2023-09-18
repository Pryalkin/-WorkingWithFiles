package com.bsuir.passport.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "application")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String number;
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;
    @EqualsAndHashCode.Include
    private String file;
    @EqualsAndHashCode.Include
    private String status;
    @EqualsAndHashCode.Include
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    private SampleApplication sampleApplication;

}
