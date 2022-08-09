package com.example.demo.exception;

import lombok.Getter;

@Getter
public enum DomainCode {

  UNKNOWN_ERROR("000", "Unknown error"),
  DEFAULT_EXCEPTION("001", ""),
  INVALID_INPUT_FIELD("002", "%s"),
  CONFLICT_REQUEST("003", "Conflict request"),
  EMPLOYEE_NOT_FOUND("004", "Employee not found with name = %s"),
  CREDENTIALS_EXCEPTION("005", "Username or Password invalid");

  private final String value;

  private final String message;

  private String activityReason;

  DomainCode(String value, String message) {
    this.value = value;
    this.message = message;
  }

  public String toUniversalCode() {
    return String.format("%s%s", ServiceIdentifier.HIERARCHY.getCode(), value);
  }
}