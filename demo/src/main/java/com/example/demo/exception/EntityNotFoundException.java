package com.example.demo.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private final DomainCode domainCode;

  private final transient Object[] args;

  public EntityNotFoundException(DomainCode domainCode, Object... args) {
    super(String.format(domainCode.getMessage(), args));
    this.domainCode = domainCode;
    this.args = args;
  }
}
