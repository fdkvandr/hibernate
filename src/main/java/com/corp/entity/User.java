package com.corp.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile", "userChats"})
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PersonalInfo personalInfo;

    @Column(unique = true, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(JsonBinaryType.class)
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserChat> userChats = new ArrayList<>();
}
