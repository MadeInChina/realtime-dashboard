package org.hanrw.app.springboot.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring boot application class for Dashboard.
 *
 * @author abaghel
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"org.hanrw.app.springboot.dashboard", "org.hanrw.app.springboot.dao", "org.hanrw.app.springboot.config"})
public class Dashboard {
    public static void main(String[] args) {
        SpringApplication.run(Dashboard.class, args);
    }
}

