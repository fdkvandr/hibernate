package com.corp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String language;

    @OneToOne
    @JoinColumn(name = "user_id") // Даже не обязательно указывать
    private User user;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
    }

}
