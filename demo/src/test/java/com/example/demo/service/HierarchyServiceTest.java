package com.example.demo.service;


import com.example.demo.infra.repository.EmployeeRepository;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HierarchyServiceTest extends Configuration {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private HierarchyService hierarchyService;

  @AfterEach
  @SneakyThrows
  void init() {
    Thread.sleep(1000);
    employeeRepository.deleteAll();
    Thread.sleep(2000);
  }



  @Test
  public void makeHierarchySuccessful() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");

    hierarchyService.makeHierarchy(hierarchyMap);
    var hierarchy = hierarchyService.getHierarchy();

    var rs = new HashMap<>();
    rs.put("Jonas",
        Map.of("Sophie",
            Map.of("Nick",
                Map.of("Pete", Map.of(),
                    "Barbara", Map.of()))));
    Assertions.assertEquals(rs, hierarchy);
  }

  @Test
  public void makeHierarchySuccessful_withInDoubleSameRequest() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");

    hierarchyService.makeHierarchy(hierarchyMap);
    var hierarchy = hierarchyService.getHierarchy();

    var rs = new HashMap<>();
    rs.put("Jonas",
        Map.of("Sophie",
            Map.of("Nick",
                Map.of("Pete", Map.of(),
                    "Barbara", Map.of()))));
    Assertions.assertEquals(rs, hierarchy);

    hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");

    hierarchyService.makeHierarchy(hierarchyMap);
    hierarchy = hierarchyService.getHierarchy();

    rs = new HashMap<>();
    rs.put("Jonas",
        Map.of("Sophie",
            Map.of("Nick",
                Map.of("Pete", Map.of(),
                    "Barbara", Map.of()))));
    Assertions.assertEquals(rs, hierarchy);
  }

  @Test
  public void makeHierarchy_WithEmployeeAndSupervisorIsTheSame() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Sophie");


    String errorMessage = null;
    try {
      hierarchyService.makeHierarchy(hierarchyMap);
    } catch (Exception exception) {
      errorMessage = exception.getMessage();
    }
    Assertions.assertNotNull(errorMessage);
    Assertions.assertEquals("Supervisor and Employee is the same with name = Sophie" , errorMessage);
  }

  @Test
  public void makeHierarchy_WithLoops() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");
    hierarchyMap.put("Jonas", "Nick");


    String errorMessage = null;
    try {
      hierarchyService.makeHierarchy(hierarchyMap);
    } catch (Exception exception) {
      errorMessage = exception.getMessage();
    }
    Assertions.assertNotNull(errorMessage);
    Assertions.assertEquals("Hierarchy is loop at Nick supervise Jonas, Jonas supervise Nick" , errorMessage);
  }

  @Test
  public void makeHierarchy_WithMultiRoots() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");
    hierarchyMap.put("David", "Navi");

    String errorMessage = null;
    try {
      hierarchyService.makeHierarchy(hierarchyMap);
    } catch (Exception exception) {
      errorMessage = exception.getMessage();
    }
    Assertions.assertNotNull(errorMessage);
    Assertions.assertEquals("Hierarchy multi root violation Jonas, Navi" , errorMessage);
  }

  @Test
  public void testGetSupervisors() {
    Map<String, String> hierarchyMap = new LinkedHashMap<>();
    hierarchyMap.put("Pete", "Nick");
    hierarchyMap.put("Barbara", "Nick");
    hierarchyMap.put("Nick", "Sophie");
    hierarchyMap.put("Sophie", "Jonas");

    hierarchyService.makeHierarchy(hierarchyMap);
    var hierarchy = hierarchyService.getHierarchy();

    var rs = new HashMap<>();
    rs.put("Jonas",
        Map.of("Sophie",
            Map.of("Nick",
                Map.of("Pete", Map.of(),
                    "Barbara", Map.of()))));
    Assertions.assertEquals(rs, hierarchy);

    var supervisors = hierarchyService.getSupervisors("Pete");

    var expectedSupervisors = new HashMap<>();
    expectedSupervisors.put("Jonas",
        Map.of("Sophie",
            Map.of("Nick", Map.of())));

    Assertions.assertEquals(expectedSupervisors, supervisors);
  }


}
