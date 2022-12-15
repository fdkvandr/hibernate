// package com.corp.entity;
//
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
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
// @AllArgsConstructor
// @NoArgsConstructor
// @PrimaryKeyJoinColumn(name = "id")
// public class Programmer extends User {
//
//     @Enumerated(EnumType.STRING)
//     private Language language;
//
//     //    @Builder
//     public Programmer(Long id, PersonalInfo personalInfo, String username, Role role, String info, Company company, Set<UserChat> userChats, List<Payment> payments, Language language) {
//         super(id, personalInfo, username, role, info, company, userChats, payments);
//         this.language = language;
//     }
// }
