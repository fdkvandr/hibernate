package com.corp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("programmer")
public class Programmer extends User{

    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(Long id, PersonalInfo personalInfo, String username, Role role, String info, Company company, Profile profile, List<UserChat> userChats, Language language) {
        super(id, personalInfo, username, role, info, company, profile, userChats);
        this.language = language;
    }
}
