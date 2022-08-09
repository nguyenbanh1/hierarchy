package com.example.demo.service.impl;

import static com.example.demo.exception.DomainCode.EMPLOYEE_NOT_FOUND;

import com.example.demo.exception.DomainBusinessException;
import com.example.demo.exception.DomainCode;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.infra.entity.Employee;
import com.example.demo.infra.repository.EmployeeRepository;
import com.example.demo.service.HierarchyService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HierarchyServiceImpl implements HierarchyService {

  private final EmployeeRepository employeeRepository;

  @Transactional
  @Override
  public void makeHierarchy(Map<String, String> hierarchyMap) {
    hierarchyMap.forEach((employeeName, supervisorName) -> {
      validateSupervisor(employeeName, supervisorName);
      var supervisor = employeeRepository.findByName(supervisorName)
          .orElse(Employee.builder().name(supervisorName).employees(new HashSet<>()).build());
      var employee = employeeRepository.findByName(employeeName)
          .orElse(Employee.builder().name(employeeName).employees(new HashSet<>()).build());
      employee.setSupervisor(supervisor);
      supervisor.getEmployees().add(employee);
      employeeRepository.saveAllAndFlush(Set.of(employee, supervisor));
      validateMultiRoot();
    });
  }

  @Override
  public Map<String, Object> getHierarchy() {
    Map<String, Object> hierarchy = new HashMap<>();
    employeeRepository.findBySupervisorIsNull()
        .forEach(employee -> hierarchy.putAll(mapEmployee(employee)));
    return hierarchy;
  }

  @Override
  public Map<String, Object> getSupervisors(String name) {
    var employee = employeeRepository.findByName(name)
        .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND, name));
    return mapSupervisor(employee);
  }

  private Map<String, Object> mapSupervisor(Employee employee) {
    Stack<String> stack = new Stack<>();

    Employee pointer = employee;
    while (pointer.getSupervisor() != null) {
      stack.push(pointer.getSupervisor().getName());
      pointer = pointer.getSupervisor();
    }
    return getSupervisors(stack);
  }

  private Map<String, Object> getSupervisors(Stack<String> supervisors) {
    if (supervisors.size() == 0) {
      return new HashMap<>();
    }
    return Map.of(supervisors.pop(), getSupervisors(supervisors));
  }

  private Map<String, Object> mapEmployee(Employee employee) {
    Map<String, Object> root = new HashMap<>();
    Map<String, Object> children = new HashMap<>();
    employee.getEmployees().forEach(e -> children.putAll(mapEmployee(e)));
    root.put(employee.getName(), children);
    return root;
  }

  private void validateSupervisor(String employeeName, String supervisorName) {
    if (employeeName.equals(supervisorName)) {
      throw new DomainBusinessException(
          String.format("Supervisor and Employee is the same with name = %s", employeeName));
    }
    var supervisor = employeeRepository.findByName(supervisorName)
        .orElse(Employee.builder().name(supervisorName).employees(new HashSet<>()).build());
    var employee = employeeRepository.findByName(employeeName)
        .orElse(Employee.builder().name(employeeName).employees(new HashSet<>()).build());
    try {
      travelSupervisor(employee, supervisor.getName());
    } catch (DomainBusinessException e) {
      String error = String.format("Hierarchy is loop at %s supervise %s, %s supervise %s",
          supervisorName, employeeName, employeeName, supervisorName);
      throw new DomainBusinessException(error);
    }
  }

  private void travelSupervisor(Employee employee, String supervisor) {
    checkSupervisor(employee.getEmployees(), supervisor);
    employee.getEmployees().forEach(item -> travelSupervisor(item, supervisor));
  }

  private void checkSupervisor(Set<Employee> employees, String supervisor) {
    employees.stream().filter(e -> e.getName().equals(supervisor)).findAny()
        .ifPresent(item -> {
          throw new DomainBusinessException(DomainCode.DEFAULT_EXCEPTION);
        });
  }

  private void validateMultiRoot() {
    List<Employee> roots = employeeRepository.findBySupervisorIsNull();
    if (roots.size() >= 2) {
      String joiningName = roots.stream().map(Employee::getName).collect(Collectors.joining(", "));
      throw new DomainBusinessException(
          String.format("Hierarchy multi root violation %s", joiningName));
    }
  }

}
