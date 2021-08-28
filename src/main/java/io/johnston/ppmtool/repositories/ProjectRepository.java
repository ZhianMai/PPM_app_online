package io.johnston.ppmtool.repositories;

import io.johnston.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

  Project findByProjectIdentifier(String projectId);

  Iterable<Project> findByProjectName(String projectName);

  @Override
  Iterable<Project> findAll();

  Iterable<Project> findAllByProjectLeader(String user);
}
