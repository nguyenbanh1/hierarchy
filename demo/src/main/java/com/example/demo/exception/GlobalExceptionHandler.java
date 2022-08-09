package com.example.demo.exception;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String ERROR = "errors";
  private static final String ERROR_CODE = "errorCode";
  private static final String ERROR_MSG = "errorMessage";

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Map<String, Object> handleException(
      HttpServletRequest request,
      Exception e) {
    log.error("method: handleException, endpoint: {}, queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return Map.of(
        ERROR, Collections.singletonList(Map.of(
            ERROR_CODE, DomainCode.UNKNOWN_ERROR.toUniversalCode(),
            ERROR_MSG, DomainCode.UNKNOWN_ERROR.getMessage())));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DomainBusinessException.class)
  public Map<String, Object> handleDomainBusinessException(
      HttpServletRequest request,
      DomainBusinessException e) {
    log.error("method: handleDomainBusinessException, endpoint: {}, queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return Map.of(
        ERROR, Collections.singletonList(Map.of(
            ERROR_CODE, e.getDomainCode().toUniversalCode(),
            ERROR_MSG, e.getMessage())));
  }

  @ExceptionHandler({
      ObjectOptimisticLockingFailureException.class,
      OptimisticLockException.class,
      DataIntegrityViolationException.class
  })
  @ResponseStatus(HttpStatus.CONFLICT)
  protected Map<String, Object> onDatabaseException(HttpServletRequest request,
      Exception exception) {

    log.error("method: onDatabaseException, endpoint: {}, queryString: {}",
        request.getRequestURI(), request.getQueryString(), exception);
    return Map.of(
        ERROR, Collections.singletonList(Map.of(
            ERROR_CODE, DomainCode.CONFLICT_REQUEST.toUniversalCode(),
            ERROR_MSG, DomainCode.CONFLICT_REQUEST.getMessage())));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, Object> handleMethodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e) {
    List<Map<String, Object>> errorDetailList = e
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> Map.<String, Object>of(
            ERROR_CODE, DomainCode.INVALID_INPUT_FIELD.toUniversalCode(),
            ERROR_MSG,
            String.format(fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                : DomainCode.INVALID_INPUT_FIELD.getMessage(), fieldError.getField())))
        .collect(Collectors.toList());

    log.error(
        "method: handleMethodArgumentNotValidException, endpoint: {}, queryString: {}, errorDetailList: {}",
        request.getRequestURI(),
        request.getQueryString(),
        errorDetailList,
        e);

    return Map.of(ERROR, errorDetailList);
  }
}
