package org.hanrw.app.kafka;

import org.hanrw.app.kafka.config.KafkaProducerProperties;
import org.hanrw.app.kafka.producer.NewUserProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaApplication implements ApplicationRunner {
    @Autowired
    private KafkaProducerProperties kafkaProducerProperties;
    @Autowired
    private NewUserProducer newUserProducer;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        newUserProducer.generateNewUsers(kafkaProducerProperties.getTopic());
    }
}
