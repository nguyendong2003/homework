package com.example.homework.controller;

import com.example.homework.exception.ProjectAlreadyExistsException;
import com.example.homework.model.Department;
import com.example.homework.model.Project;
import com.example.homework.service.DepartmentService;
import com.example.homework.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final DepartmentService departmentService;

    public ProjectController(ProjectService projectService, DepartmentService departmentService) {
        this.projectService = projectService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer departmentId,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectService.searchProjects(projectName, difficulty, departmentId, pageable);

        model.addAttribute("projects", projects);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("projectName", projectName);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("departmentId", departmentId);

        return "projects/list";
    }

    // Create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "projects/create";
    }

    @PostMapping("/create/confirm")
    public String confirmCreateProject(@Valid @ModelAttribute Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/create";
        }

        // Chỗ này để hiển thị ra tên phòng ban trong confirm
        if (project.getDepartment() != null && project.getDepartment().getId() != null) {
            Department department = departmentService.getDepartmentById(project.getDepartment().getId());
            project.setDepartment(department);
        }

        model.addAttribute("project", project);
        return "projects/confirm-create";
    }

    @PostMapping("/create")
    public String createProject(@Valid @ModelAttribute Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/create";
        }

        try {
            projectService.createProject(project);
            model.addAttribute("message", "Dự án đã được tạo thành công!");
        } catch (ProjectAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/create";
        }

        return "projects/complete";
    }

    // Update
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        projectService.getProjectById(id).ifPresentOrElse(
                project -> {
                    model.addAttribute("project", project);
                    model.addAttribute("departments", departmentService.getAllDepartments());
                },
                () -> model.addAttribute("error", "Dự án không tồn tại!")
        );
        return "projects/update";
    }

    @PostMapping("/update/confirm")
    public String confirmUpdateProject(@Valid @ModelAttribute Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/update";
        }

        // Chỗ này để hiển thị ra tên phòng ban trong confirm
        if (project.getDepartment() != null && project.getDepartment().getId() != null) {
            Department department = departmentService.getDepartmentById(project.getDepartment().getId());
            project.setDepartment(department);
        }

        model.addAttribute("project", project);
        return "projects/confirm-update";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable Integer id, @Valid @ModelAttribute Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/update";
        }

        try {
            projectService.updateProject(id, project);
            model.addAttribute("message", "Dự án đã được cập nhật thành công!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "projects/update";
        }

        return "projects/complete";
    }

    @GetMapping("/delete/confirm/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        projectService.getProjectById(id).ifPresentOrElse(
                project -> model.addAttribute("project", project),
                () -> model.addAttribute("error", "Dự án không tồn tại!")
        );
        return "projects/confirm-delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Model model) {
        try {
            projectService.deleteProject(id);
            model.addAttribute("message", "Dự án đã được xóa thành công!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "projects/list";
        }
        return "projects/complete";
    }
}