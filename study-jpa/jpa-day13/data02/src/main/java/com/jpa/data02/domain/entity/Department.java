package com.jpa.data02.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "JPA_DEPARTMENT")
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {
    @Id @GeneratedValue
    @Column(name = "DEPARTMENT_ID")
    Long id;
    String name;
    String phone;
    String officeLocation;

    @Builder
    public Department(Long id, String name, String phone, String officeLocation) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.officeLocation = officeLocation;
    }
}


