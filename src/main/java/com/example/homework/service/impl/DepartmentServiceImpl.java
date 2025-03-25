package com.example.homework.service.impl;

import com.example.homework.exception.DepartmentNotFoundException;
import com.example.homework.model.Department;
import com.example.homework.repository.DepartmentRepository;
import com.example.homework.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Phòng ban không tồn tại với ID: " + id));
    }

    @Override
    public void saveDepartment(@Valid Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Integer id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Không tìm thấy phòng ban để xóa với ID: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
