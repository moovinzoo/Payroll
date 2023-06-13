package com.example.payroll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Employee("DJ", "Developer")));
            log.info("Preloading " + repository.save(new Employee("JH", "Doctor")));
        };
    }
}