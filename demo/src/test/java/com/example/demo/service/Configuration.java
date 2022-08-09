package com.example.demo.service;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@EnableAutoConfiguration
@ContextConfiguration(initializers = Configuration.TestEnvInitializer.class)
@DirtiesContext
public class Configuration {

  @Container
  private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8")
      .withDatabaseName("hierarchy_company_db")
      .withUsername("root")
      .withPassword("password");

  static {
    mySQLContainer.start();
  }

  @DynamicPropertySource
  static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
  }

  static class TestEnvInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    @SneakyThrows
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues values = TestPropertyValues.of(
          "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
          "spring.datasource.password=" + mySQLContainer.getPassword(),
          "spring.datasource.username=" + mySQLContainer.getUsername()
      );
      values.applyTo(applicationContext);
      Thread.sleep(12000);
    }

  }

}
