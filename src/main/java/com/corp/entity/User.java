package com.corp.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;

import static com.corp.util.StringUtils.SPACE;


@NamedEntityGraph(name = "WithCompanyAndChat", attributeNodes = {@NamedAttributeNode("company"), @NamedAttributeNode(value = "userChats", subgraph = "chats")}, subgraphs = {@NamedSubgraph(name = "chats", attributeNodes = @NamedAttributeNode("chat"))})
@NamedEntityGraph(name = "WithCompany", attributeNodes = {@NamedAttributeNode("company")})
@FetchProfile(name = "withCompany", fetchOverrides = {@FetchProfile.FetchOverride(entity = User.class, association = "company", mode = FetchMode.JOIN)})
@FetchProfile(name = "withCompanyAndPayments", fetchOverrides = {@FetchProfile.FetchOverride(entity = User.class, association = "company", mode = FetchMode.JOIN), @FetchProfile.FetchOverride(entity = User.class, association = "payments", mode = FetchMode.JOIN)})
@NamedQuery(name = "findUserByName", query = "SELECT u " + "FROM User u " + "WHERE u.personalInfo.firstname = :firstname " + "AND u.company.name = :companyName " + "ORDER BY u.personalInfo.lastname DESC")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "payments", "userChats"})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Builder
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
public class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    private PersonalInfo personalInfo;

    @NotNull
    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private Profile profile;

    @Builder.Default
    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<UserChat> userChats = new ArrayList<>();

    @NotAudited
    @Builder.Default
    // @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}