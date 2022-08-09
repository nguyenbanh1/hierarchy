package com.example.demo.infra.repository;

import com.example.demo.infra.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

  Optional<Employee> findByName(String name);

  List<Employee> findBySupervisorIsNull();
}
