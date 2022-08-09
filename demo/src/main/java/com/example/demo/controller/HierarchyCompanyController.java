package com.example.demo.controller;

import com.example.demo.service.HierarchyService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hierarchy")
@RequiredArgsConstructor
public class HierarchyCompanyController {

  private final HierarchyService hierarchyService;

  @PostMapping
  public void makeHierarchy(@RequestBody Map<String, String> hierarchyMap) {
    hierarchyService.makeHierarchy(hierarchyMap);
  }

  @GetMapping
  public Map<String, Object> getHierarchy() {
    return hierarchyService.getHierarchy();
  }

}
