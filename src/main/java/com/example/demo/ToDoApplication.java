package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class ToDoApplication {

    public static void main(String[] args) {
        System.out.println("=== MAIN STARTED 2 ===");
        log.info("=== MAIN STARTED 2 ===");
        SpringApplication.run(ToDoApplication.class, args);
        log.info("=== MAIN ENDED ===");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        log.info("Application started !!!!!!!!");
    }

}
