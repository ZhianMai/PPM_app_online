package io.johnston.ppmtool.services;

import io.johnston.ppmtool.domain.Backlog;
import io.johnston.ppmtool.domain.Project;
import io.johnston.ppmtool.domain.ProjectTask;
import io.johnston.ppmtool.exceptions.ProjectDateException;
import io.johnston.ppmtool.exceptions.ProjectNotFoundException;
import io.johnston.ppmtool.repositories.BacklogRepository;
import io.johnston.ppmtool.repositories.ProjectRepository;
import io.johnston.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectTaskService {

  @Autowired
  private ProjectTaskRepository projectTaskRepository;

  @Autowired
  private ProjectService projectService;

  public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,
                                    String username) {
    // Project task to be added to a specific project, project != null
    try {
      // If project != null, then backlog != null
      Project project = projectService.findProjectByIdentifier(projectIdentifier, username);
      Date projectBeginDate = project.getStart_date();
      Date taskDueDate = projectTask.getDueDate();

      if (projectBeginDate != null && taskDueDate != null &&
          projectBeginDate.compareTo(taskDueDate) > 0) {
        throw new ProjectDateException("The task due day is earlier than the project begin day." );
      }

      Backlog backlog = project.getBacklog();
      // Set the backlog to project
      projectTask.setBacklog(backlog);

      // The project task sequence starts from 0, if i is deleted, then new
      // task should not use i again.
      Integer backlogSequence = backlog.getPTSequence();
      backlogSequence++;
      backlog.setPTSequence(backlogSequence);

      // Add sequence to project task
      projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
      projectTask.setProjectIdentifier(projectIdentifier);

      // Init priority is null
      if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
        projectTask.setPriority(3);
      }

      // Init status is null
      if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
        projectTask.setStatus("TO_DO");
      }

      return projectTaskRepository.save(projectTask);
    } catch (ProjectDateException e) {
      throw new ProjectDateException(e.getMessage());
    }
    catch (Exception e) {
      throw new ProjectNotFoundException("Project not found");
    }
  }

  public List<ProjectTask> findBacklogById(String project_id, String username) {
    projectService.findProjectByIdentifier(project_id, username);

    return projectTaskRepository.findByProjectIdentifierOrderByPriority(project_id);
  }

  public ProjectTask findProjectTaskByProjectSequence(String project_id, String projectTask_id,
                                                      String username) {
    // Check if project id exists
    // Old way to implement, but now take the benefit of finding project by checking username
    /*
    Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
    if (backlog == null) {
      throw new ProjectNotFoundException("Project ID " + backlog_id + " doesnot exist.");
    }
    */
    projectService.findProjectByIdentifier(project_id, username);

    // Check if project task id exists
    ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTask_id);
    if (projectTask == null) {
      throw new ProjectNotFoundException("Project task id: " + projectTask_id + " not found");
    }

    // Check if the project task is under the project
    if (!projectTask.getProjectIdentifier().equals(project_id)) {
      throw new ProjectNotFoundException("Project task id: " + projectTask_id +
          " is not under project id " + project_id + ".");
    }

    return projectTask;
  }

  public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String project_id,
                                             String projectTask_id, String username) {
    // For validation project_id and projectTask_id, so ignore return value
    findProjectTaskByProjectSequence(project_id, projectTask_id, username);
    // Update project task
    return projectTaskRepository.save(updatedProjectTask);
  }

  public void deleteProjectTaskByProjectSequence(String project_id, String projectTask_id,
                                                 String username) {
    ProjectTask projectTask =
        findProjectTaskByProjectSequence(project_id, projectTask_id, username);

    /*
    Backlog backlog = projectTask.getBacklog();
    List<ProjectTask> projectTaskList = backlog.getProjectTaskList();
    projectTaskList.remove(projectTask);
    backlogRepository.save(backlog);
     */

    projectTaskRepository.delete(projectTask);
  }
}
