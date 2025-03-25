package com.example.homework.service;

import com.example.homework.model.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Integer id);
    void saveDepartment(Department department);
    void deleteDepartment(Integer id);
}