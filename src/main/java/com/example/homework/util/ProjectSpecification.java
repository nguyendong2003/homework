package com.example.homework.util;

import org.springframework.data.jpa.domain.Specification;
import com.example.homework.model.Project;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {
    public static Specification<Project> filterBy(String projectName, String difficulty, Integer departmentId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (projectName != null && !projectName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("projectName"), "%" + projectName + "%"));
            }

            if (difficulty != null && !difficulty.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("difficulty"), difficulty));
            }

            if (departmentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), departmentId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

