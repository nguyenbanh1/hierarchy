package com.example.demo.service;

import java.util.Map;
import java.util.Stack;

public interface HierarchyService {

  void makeHierarchy(Map<String, String> hierarchyMap);

  Map<String, Object> getHierarchy();

  Map<String, Object> getSupervisors(String name);
}
