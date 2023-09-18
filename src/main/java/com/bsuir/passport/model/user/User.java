package com.bsuir.passport.model.user;

import com.bsuir.passport.model.Person;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Include
    private String password;
    @EqualsAndHashCode.Include
    private String role;
    @EqualsAndHashCode.Include
    private String[] authorities;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Person person;

}
