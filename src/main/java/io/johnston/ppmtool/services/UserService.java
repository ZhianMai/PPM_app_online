package io.johnston.ppmtool.services;

import io.johnston.ppmtool.domain.User;
import io.johnston.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.johnston.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public User saveUser(User newUser) {

    // Username has to be unique, throw exception.
    try {
      newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

      newUser.setUsername(newUser.getUsername());
      // Set confirmPassword to empty to avoid returning the real password back.
      // Cannot @JsonIgnore on confirmPassword, since ignore before validation
      // It's always not match since confirmPassowrd is null.
      newUser.setConfirmPassword("");
      return userRepository.save(newUser);
    } catch (Exception e) {
      throw new UsernameAlreadyExistsException("Username: " + newUser.getUsername() +
          " already exists");
    }
    // Confirm password match.


  }
}
