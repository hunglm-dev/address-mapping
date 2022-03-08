package com.hunglm.address.mapping;

import com.hunglm.address.mapping.schedules.IndexRefresher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AddressMappingApplication implements ApplicationRunner {

  @Autowired
  IndexRefresher indexRefresher;

  public static void main(String[] args) {
    SpringApplication.run(AddressMappingApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    indexRefresher.refreshingData();
  }
}
