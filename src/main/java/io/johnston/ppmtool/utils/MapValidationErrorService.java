package io.johnston.ppmtool.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class MapValidationErrorService {

  public static ResponseEntity<?> mapValidationService(BindingResult result) {
    // Returning a meaningful JSON obj can let frontend React work easier.
    if (result.hasErrors()) {
      // return new ResponseEntity<String>("Invalid Project object", HttpStatus.BAD_REQUEST);
      // JSON pattern
      // {
      //    "field": "error message, value: ..."
      // }
      Map<String, String> errorMap = new HashMap<>();

      for (FieldError error: result.getFieldErrors()) {
        String message = error.getDefaultMessage();
        Object rejectVal = error.getRejectedValue();
        message += "; value: ";

        if (rejectVal != null) {
          message += rejectVal.toString();
        } else {
          message += "null";
        }

        errorMap.put(error.getField(), message);
      }

      return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
    }

    return null;
  }
}
