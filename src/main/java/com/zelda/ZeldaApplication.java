package com.zelda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@ComponentScan(basePackages = {"com.zelda"})
@EnableJpaRepositories({"com.zelda"})
@EntityScan({"com.zelda"})
public class ZeldaApplication {
  public static void main(String[] args) {
    try {
      SpringApplication.run(ZeldaApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
