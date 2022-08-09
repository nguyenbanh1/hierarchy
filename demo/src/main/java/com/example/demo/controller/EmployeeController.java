package com.example.demo.controller;


import com.example.demo.service.HierarchyService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

  private final HierarchyService hierarchyService;

  @GetMapping("/{name}/supervisors")
  public Map<String, Object> getSupervisors(@PathVariable("name") String name) {
    return hierarchyService.getSupervisors(name);
  }

}
