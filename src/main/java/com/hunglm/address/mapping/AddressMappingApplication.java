package com.hunglm.address.mapping;

import com.hunglm.address.mapping.schedules.IndexRefresher;
import com.hunglm.address.mapping.services.HisConnectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AddressMappingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressMappingApplication.class, args);
    }

}
