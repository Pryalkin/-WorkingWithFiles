package com.bsuir.passport.model;

import com.bsuir.passport.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "person")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String name;
    @EqualsAndHashCode.Include
    private String surname;
    @EqualsAndHashCode.Include
    private String patronymic;
    @EqualsAndHashCode.Include
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy", timezone = "Europe/Minsk")
    private Date dateOfBirth;
    @EqualsAndHashCode.Include
    private String passportSeries;
    @EqualsAndHashCode.Include
    private String passportNumber;
    @EqualsAndHashCode.Include
    private String imagePassport;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(
            mappedBy = "person",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Application> applications = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
        user.setPerson(this);
    }

    public void addApp(Application app){
        applications.add(app);
        app.setPerson(this);
    }
}
