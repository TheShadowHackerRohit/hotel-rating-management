package com.example.user.service.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "micro_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "ID")
    private String userId;

    @Column(name = "NAME",length = 50)
    private String name;

    @Column(name = "EMAIL",length = 50)
    private String email;

    @Column(name = "ABOUT",length = 50)
    private String about;

    @Transient
    private List<Rating> ratingList = new ArrayList<>();


}
