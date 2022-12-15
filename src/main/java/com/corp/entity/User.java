package com.corp.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.corp.util.StringUtils.SPACE;


@NamedQuery(name = "findUserByName", query =
        "SELECT u " +
        "FROM User u " +
        "WHERE u.personalInfo.firstname = :firstname " +
        "AND u.company.name = :companyName " +
        "ORDER BY u.personalInfo.lastname DESC")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "payments", "userChats"})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Builder
public class User implements Comparable<User>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String info;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}