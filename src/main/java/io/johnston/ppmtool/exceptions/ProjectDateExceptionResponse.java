package io.johnston.ppmtool.exceptions;

public class ProjectDateExceptionResponse {

  private String end_date;

  public ProjectDateExceptionResponse(String end_date) {
    this.end_date = end_date;
  }

  public String getEnd_date() {
    return end_date;
  }

  public void setEnd_date(String end_date) {
    this.end_date = end_date;
  }
}
