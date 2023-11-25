package com.jpa.basic3.domain;

import com.jpa.basic3.domain.base.Period;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "JPA_PRODUCT")
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑(조인 전략), 부모 클래스에서 사용
@DiscriminatorColumn(name = "DTYPE") // 안해도 디폴트가 DTYPE임. 부모테이블의 컬럼 중 타입을 저장할 컬럼을 설정한다.(구분 컬럼)
@AttributeOverrides({ // @MappedSuperClass를 통해 상속받은 필드를 재정의하기 위해 사용(컬럼이름 수정)
        @AttributeOverride(name = "createdDate", column = @Column(name = "PRODUCT_CREATED_DATE")),
        @AttributeOverride(name = "modifiedDate", column = @Column(name = "PRODUCT_MODIFIED_DATE"))
})
public class Product extends Period {
    @Id @GeneratedValue @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;
    private int price;
}
