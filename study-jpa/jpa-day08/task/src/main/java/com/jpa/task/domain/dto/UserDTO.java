package com.jpa.task.domain.dto;

import com.jpa.task.domain.enumType.UserGender;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data @NoArgsConstructor
public class UserDTO {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String address;
    private String addressDetail;
    private String zipcode;
    private UserGender gender;
}
