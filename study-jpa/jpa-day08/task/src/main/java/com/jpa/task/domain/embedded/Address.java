package com.jpa.task.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @Getter
public class Address {
    private String address;
    private String addressDetail;
    private String zipcode;
}
