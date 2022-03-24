package me.study.jwttutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String nickname;

    @JsonIgnore
    private boolean activated;

    @ManyToMany
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name"))
    private Set<Authority> authorities = new java.util.LinkedHashSet<>();
}
