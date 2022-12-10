package com.corp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {

    private String projectName;

//    @Builder
    public Manager(Long id, PersonalInfo personalInfo, String username, Role role, String info, Company company, Profile profile, List<UserChat> userChats, List<Payment> payments, String projectName) {
        super(id, personalInfo, username, role, info, company, profile, userChats, payments);
        this.projectName = projectName;
    }
}
