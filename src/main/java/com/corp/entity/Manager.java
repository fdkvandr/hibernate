// package com.corp.entity;
//
// import jakarta.persistence.Entity;
// import jakarta.persistence.PrimaryKeyJoinColumn;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import lombok.NoArgsConstructor;
//
// import java.util.List;
// import java.util.Set;
//
// @Entity
// @Data
// @EqualsAndHashCode(callSuper = false)
// @NoArgsConstructor
// @AllArgsConstructor
// @PrimaryKeyJoinColumn(name = "id")
// public class Manager extends User {
//
//     private String projectName;
//
//     //    @Builder
//     public Manager(Long id, PersonalInfo personalInfo, String username, Role role, String info, Company company, Set<UserChat> userChats, List<Payment> payments, String projectName) {
//         super(id, personalInfo, username, role, info, company, userChats, payments);
//         this.projectName = projectName;
//     }
// }
