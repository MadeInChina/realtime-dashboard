package org.hanrw.app.spark;

import org.hanrw.app.spark.processor.NewUserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparkApplication implements ApplicationRunner {
    @Autowired
    private NewUserProcessor newUserProducer;

    public static void main(String[] args) {
        SpringApplication.run(SparkApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        newUserProducer.start();
    }
}
