package com.example.homework.service.impl;

import com.example.homework.exception.ProjectAlreadyExistsException;
import com.example.homework.model.Project;
import com.example.homework.repository.ProjectRepository;
import com.example.homework.service.ProjectService;
import com.example.homework.util.ProjectSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    @Override
    public Page<Project> searchProjects(String projectName, String difficulty, Integer departmentId, Pageable pageable) {
        Specification<Project> spec = ProjectSpecification.filterBy(projectName, difficulty, departmentId);
        return projectRepository.findAll(spec, pageable);
    }

    @Override
    public void createProject(Project project) {
        if (projectRepository.existsById(project.getId())) {
            throw new ProjectAlreadyExistsException("Dự án với ID " + project.getId() + " đã tồn tại!");
        }
        projectRepository.save(project);
    }

    @Override
    public void updateProject(Integer id, Project project) {
        Project existingProject = projectRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Không tìm thấy dự án với ID: " + id)
        );

        existingProject.setProjectName(project.getProjectName());
        existingProject.setDifficulty(project.getDifficulty());
        existingProject.setVersion(project.getVersion());
        existingProject.setDepartment(project.getDepartment());

        projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy dự án với ID: " + id);
        }
        projectRepository.deleteById(id);
    }
}
