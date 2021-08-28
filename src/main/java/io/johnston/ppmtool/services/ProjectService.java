package io.johnston.ppmtool.services;

import io.johnston.ppmtool.domain.Backlog;
import io.johnston.ppmtool.domain.Project;
import io.johnston.ppmtool.domain.User;
import io.johnston.ppmtool.exceptions.ProjectDateException;
import io.johnston.ppmtool.exceptions.ProjectIdException;
import io.johnston.ppmtool.exceptions.ProjectNotFoundException;
import io.johnston.ppmtool.repositories.BacklogRepository;
import io.johnston.ppmtool.repositories.ProjectRepository;
import io.johnston.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ProjectService {

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private BacklogRepository backlogRepository;

  @Autowired
  private UserRepository userRepository;

  public Project saveOrUpdateProject(Project project, String username) {
    if (project.getId() != null) {
      Project existingProject =
          projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

      if (existingProject != null && !(existingProject.getProjectLeader().equals(username))) {
        throw new ProjectNotFoundException("Project not found in your account");
      } else if (existingProject == null) {
        throw new ProjectNotFoundException("Project with id: " +
            project.getProjectIdentifier() + " does not exist");
      }
    }

    if (project.getStart_date() != null && project.getEnd_date() != null &&
       project.getEnd_date().compareTo(project.getStart_date()) < 0) {
      throw new ProjectDateException("Project end date is before project start day.");
    }

    String tempProjectIdentifier = project.getProjectIdentifier().toUpperCase();

    try {
      User user = userRepository.findByUsername(username);

      project.setUser(user);
      project.setProjectLeader(user.getUsername());
      project.setProjectIdentifier(tempProjectIdentifier);

      if (project.getId() == null) {
        Backlog backlog = new Backlog();
        project.setBacklog(backlog);
        backlog.setProject(project);
        backlog.setProjectIdentifier(tempProjectIdentifier);
      }

      if (project.getId() != null) {
        project.setBacklog(backlogRepository.findByProjectIdentifier(tempProjectIdentifier));
      }

      return projectRepository.save(project);
    } catch (Exception e) {
      throw new ProjectIdException("Project ID '" +
          project.getProjectIdentifier().toUpperCase() + "' already exists");
    }
  }

  public Project findProjectByIdentifier(String projectId, String username) {
    Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

    if (project == null) {
      // Using the old exception so far
      throw new ProjectIdException("Project ID '" + projectId + "' does not exist.");
    }

    if (!project.getProjectLeader().equals(username)) {
      throw new ProjectNotFoundException("Project not found in your account.");
    }

    return project;
  }

  public Iterable<Project> findProjectByName(String projectName) {
    return projectRepository.findByProjectName(projectName);
  }

  public Iterable<Project> findAllProjects(String username) {
    return projectRepository.findAllByProjectLeader(username);
  }

  public void deleteProjectByIdentifier(String projectId, String username) {
    /*
    // Redundant code before using JWT
    Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

    if (project == null) {
      // Using the old exception so far
      throw new ProjectIdException("Cannot delete project with ID '" + projectId +
          "': does not exist.");
    }
    */


    projectRepository.delete(findProjectByIdentifier(projectId, username));
  }
}
