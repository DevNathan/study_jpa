package com.jpa.task.domain.entity;

import com.jpa.task.domain.base.Period;
import com.jpa.task.domain.embedded.Address;
import com.jpa.task.domain.enumType.UserGender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "JPA_TABLE")
@Getter @Setter @ToString
@NoArgsConstructor
@SequenceGenerator(
        name = "SEQ_USER_GENERATOR",
        sequenceName = "SEQ_USER"
)
@DynamicInsert // 필드에 null이 들어가 있다면 INSERT쿼리에서 해당 필드를 제외한다.
@DynamicUpdate // 필드에 null이 들어가 있다면 UPDATE쿼리에서 해당 필드를 제외한다.
public class User extends Period {
    @Id @GeneratedValue(generator = "SEQ_USER_GENERATOR")
    @Column(name = "USER_ID")
    private Long id;
    @Column(unique = true, nullable = false)
    private String loginId;
    private String password;
    private String name;

    @Embedded
    @AttributeOverrides({ //not null 제약조건을 걸기 위해 사용했음 (Address 타입의 필드에 직접 설정)
            @AttributeOverride(name = "address", column = @Column(nullable = false)),
            @AttributeOverride(name = "addressDetail", column = @Column(nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(nullable = false))
    })
    private Address address;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'") // 컬럼의 디폴트 값을 설정한다.
    private UserGender gender;
}
