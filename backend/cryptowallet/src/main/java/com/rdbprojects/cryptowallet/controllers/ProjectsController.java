package com.rdbprojects.cryptowallet.controllers;

import com.rdbprojects.cryptowallet.dao.ProjectsDao;
import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.entities.Projects;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProjectsController {
    @Autowired
    ProjectsDao projectsDao;

    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/projects")
    public ResponseEntity<List<Projects>> getAllProjects() {
        List<Projects> projects = projectsDao.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/project")
    public ResponseEntity<Projects> getProjectsByProjectName() {
        // check request

        // try to find project
        List<Projects> findedProject = projectsDao.findAll();
        if (findedProject == null) {
            return ResponseEntity.status(400).eTag("Project didn't find ...").body(null);
        }
        if (findedProject.isEmpty()) {
            return ResponseEntity.status(400).eTag("Project didn't find ...").body(null);
        }
        return ResponseEntity.ok(findedProject.get(0));
    }

    @PostMapping("/project")
    public ResponseEntity<Projects> addProject(@RequestHeader(value="${json.token}") String token, @RequestBody Projects projectDetails) {
        // check request
        if (projectDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (projectDetails.getProjectName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!user.getIsAdmin()) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Projects findedProject = projectsDao.findProjectByName(projectDetails);
        if (findedProject == null) {
            Projects addedProject = projectsDao.addProject(projectDetails);
            return ResponseEntity.ok(addedProject);
        }
        return ResponseEntity.status(400).eTag("Projects already exist ...").body(null);
    }

    @PutMapping("/project")
    public ResponseEntity<Projects> updateProject(@RequestHeader(value="${json.token}") String token, @RequestBody Projects projectDetails) {
        // check request
        if (projectDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (projectDetails.getProjectName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!user.getIsAdmin()) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Projects findedProject = projectsDao.findProjectByName(projectDetails);
        if (findedProject != null) {
            findedProject.setProjectDescription(projectDetails.getProjectDescription());

            Projects updatedProject = projectsDao.updateProject(findedProject);

            return ResponseEntity.ok(updatedProject);
        }
        return ResponseEntity.status(400).eTag("Project didn't find ...").body(null);

    }

    @DeleteMapping("/project")
    public ResponseEntity<String> deleteProject(@RequestHeader(value="${json.token}") String token, @RequestBody Projects projectDetails) {

        // check request
        if (projectDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        if (projectDetails.getProjectName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
            }
            if (!user.getIsAdmin() ) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        //
        Projects deletedProject = projectsDao.deleteProject(projectDetails);
        if (deletedProject != null) {
            return ResponseEntity.ok("Project deleted successfully");
        }
        return ResponseEntity.status(400).eTag("Project didn't find ...").body("Project didn't find ...");


    }
}
