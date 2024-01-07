package com.jpa.data02.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentDto {
    private String name;
    private String phoneNumber;
    private String office;

    public DepartmentDto(String name, String phoneNumber, String office) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.office = office;
    }
}
