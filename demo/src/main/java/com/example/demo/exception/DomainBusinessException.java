package com.example.demo.exception;

public class DomainBusinessException extends RuntimeException {

  private final DomainCode domainCode;

  private final transient Object[] args;

  public DomainBusinessException(DomainCode domainCode, Object... args) {
    super(String.format(domainCode.getMessage(), args));
    this.domainCode = domainCode;
    this.args = args;
  }

  public DomainBusinessException(String message) {
    super(message);
    this.domainCode = DomainCode.DEFAULT_EXCEPTION;
    this.args = new Object[]{};
  }

  public DomainCode getDomainCode() {
    return domainCode;
  }

}
