package com.bsuir.passport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sample_application")
public class SampleApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String url;
    private String name;
    private String text;
    @OneToMany(
            mappedBy = "sampleApplication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Application> applications = new ArrayList<>();

    public void addApp(Application app){
        applications.add(app);
        app.setSampleApplication(this);
    }
}
