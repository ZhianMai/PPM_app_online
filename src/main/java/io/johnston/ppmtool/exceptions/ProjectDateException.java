package io.johnston.ppmtool.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectDateException extends RuntimeException {

  public ProjectDateException(String message) {
    super(message);
  }

}