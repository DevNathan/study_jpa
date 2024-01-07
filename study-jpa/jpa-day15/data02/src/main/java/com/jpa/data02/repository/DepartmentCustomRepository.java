package com.jpa.data02.repository;

import com.jpa.data02.domain.entity.Department;

import java.util.List;

public interface DepartmentCustomRepository {
    List<Department> findCustomByName(String name);
}
