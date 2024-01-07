package com.jpa.data02.repository;

import com.jpa.data02.domain.entity.Employee;
import com.jpa.data02.domain.entity.QEmployee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.jpa.data02.domain.entity.QEmployee.employee;

@Repository
public class EmployeeJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public EmployeeJpaRepository(EntityManager em) {
//        JPAQueryFactory는 EntityManager를 받아야하므로 em을 주입하는 생성자를 통해 초기화해준다.
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public Employee save(Employee employee){
        em.persist(employee);
        return employee;
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(employee).where(employee.id.eq(id)).fetchOne()
        );
    }

    public List<Employee> findByName(String name) {
        return queryFactory.selectFrom(employee).where(employee.name.eq(name)).fetch();
    }

    public List<Employee> findAll() {
        return queryFactory.selectFrom(employee).fetch();
    }


}
