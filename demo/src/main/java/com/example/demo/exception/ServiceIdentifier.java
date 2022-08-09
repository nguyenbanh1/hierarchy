package com.example.demo.exception;

public enum ServiceIdentifier {
  HIERARCHY("32");

  private final String code;

  ServiceIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
