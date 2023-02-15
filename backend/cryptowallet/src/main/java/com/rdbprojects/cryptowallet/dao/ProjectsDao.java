package com.rdbprojects.cryptowallet.dao;

import com.rdbprojects.cryptowallet.entities.Projects;
import com.rdbprojects.cryptowallet.repositories.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsDao {
    @Autowired
    ProjectsRepository projectsRepository;

    public Projects addProject(Projects project) {
        try {
            Projects savedProject = projectsRepository.save(project);
            return savedProject;
        } catch (Exception e) {
            return null;
        }

    }

    public Projects findProjectByName(Projects project) {
        try {
            Projects findedProject = projectsRepository.findProjectByName(project.getProjectName());
            return findedProject;
        } catch (Exception e) {
            return null;
        }

    }

    public Projects updateProject(Projects project) {
        try {
            Projects updatedProject = projectsRepository.save(project);
            return updatedProject;
        } catch (Exception e) {
            return null;
        }

    }

    public Projects deleteProject(Projects project) {
        try {
            projectsRepository.delete(project);
            return project;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Projects> findAll() {
        try {
            List<Projects> findedProjects = projectsRepository.findAll();
            return findedProjects;
        } catch (Exception e) {
            return null;
        }

    }

}
