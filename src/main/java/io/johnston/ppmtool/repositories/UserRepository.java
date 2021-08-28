package io.johnston.ppmtool.repositories;

import io.johnston.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  User findByUsername(String username);
  User getById(Long id);
  // Optional: avoid returning null
  Optional<User> findById(Long id);
}
