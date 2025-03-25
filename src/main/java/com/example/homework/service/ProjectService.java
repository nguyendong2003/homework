package com.example.homework.service;

import com.example.homework.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Integer id);
    void createProject(Project project);
    void updateProject(Integer id, Project project);
    void deleteProject(Integer id);
    public Page<Project> searchProjects(String projectName, String difficulty, Integer departmentId, Pageable pageable);
}