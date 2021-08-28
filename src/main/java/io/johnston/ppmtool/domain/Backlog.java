package io.johnston.ppmtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Backlog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer PTSequence = 0;
  private String projectIdentifier;

  // One-to-One backlog - project
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id", nullable = false)
  /* Without this, an infinity loop would occur: project -> backlog -> project */
  @JsonIgnore
  private Project project;
  // One-to-Many project - project tasks

  @OneToMany(cascade = CascadeType.REFRESH,
             fetch = FetchType.EAGER,
             mappedBy = "backlog",
             orphanRemoval = true)
  private List<ProjectTask> projectTaskList = new ArrayList<>();

  public Backlog() {
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPTSequence() {
    return PTSequence;
  }

  public void setPTSequence(Integer PTSequence) {
    this.PTSequence = PTSequence;
  }

  public String getProjectIdentifier() {
    return projectIdentifier;
  }

  public void setProjectIdentifier(String projectIdentifier) {
    this.projectIdentifier = projectIdentifier;
  }

  public List<ProjectTask> getProjectTaskList() {
    return projectTaskList;
  }

  public void setProjectTaskList(List<ProjectTask> projectTaskList) {
    this.projectTaskList = projectTaskList;
  }

  @Override
  public String toString() {
    return "Backlog{" +
        "id=" + id +
        ", PTSequence=" + PTSequence +
        ", projectIdentifier='" + projectIdentifier + '\'' +
        '}';
  }
}
