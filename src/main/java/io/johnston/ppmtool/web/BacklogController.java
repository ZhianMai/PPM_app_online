package io.johnston.ppmtool.web;

import io.johnston.ppmtool.domain.Project;
import io.johnston.ppmtool.domain.ProjectTask;
import io.johnston.ppmtool.services.ProjectTaskService;
import io.johnston.ppmtool.utils.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

  @Autowired
  private ProjectTaskService projectTaskService;

  private MapValidationErrorService mapValidationErrorService;

  @PostMapping("/{project_id}")
  public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                   BindingResult result,
                                                   @PathVariable String project_id,
                                                   Principal principal) {
    ResponseEntity<?> errorMap = MapValidationErrorService.mapValidationService(result);

    if (errorMap != null) {
      return errorMap;
    }

    ProjectTask projectTask1 =
        projectTaskService.addProjectTask(project_id, projectTask, principal.getName());

    return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
  }

  @GetMapping("/{project_id}")
  public Iterable<ProjectTask> getProjectBacklog(@PathVariable String project_id,
                                                 Principal principal) {
    return projectTaskService.findBacklogById(project_id, principal.getName());
  }

  @GetMapping("/{project_id}/{projectTask_id}")
  public ResponseEntity<?> getProjectTask(@PathVariable String project_id,
                                          @PathVariable String projectTask_id,
                                          Principal principal) {
    ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(project_id,
        projectTask_id, principal.getName());

    return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
  }

  @PatchMapping("/{project_id}/{projectTask_id}")
  public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask,
                                             BindingResult result,
                                             @PathVariable String project_id,
                                             @PathVariable String projectTask_id,
                                             Principal principal) {

    ResponseEntity<?> errorMap = MapValidationErrorService.mapValidationService(result);

    if (errorMap != null) {
      return errorMap;
    }

    ProjectTask updatedTask =
        projectTaskService.updateByProjectSequence(updatedProjectTask, project_id,
                                                   projectTask_id, principal.getName());

    return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
  }

  @DeleteMapping("/{project_id}/{projectTask_id}")
  public ResponseEntity<?> deleteProjectTask(@PathVariable String project_id,
                                             @PathVariable String projectTask_id,
                                             Principal principal) {
    projectTaskService.deleteProjectTaskByProjectSequence(project_id, projectTask_id,
                                                          principal.getName());

    return new ResponseEntity<String>("Project task with id " + projectTask_id + " deleted",
        HttpStatus.OK);
  }
}
