package xyz.hw2.myledger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    private static final String DEFAULT_URL = "http://localhost:8080/login.html";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("------------------------------------");
        System.out.println("Spring Boot Application started successfully!");
        System.out.println("Access your application at: " + DEFAULT_URL);
        System.out.println("------------------------------------");
    }
}