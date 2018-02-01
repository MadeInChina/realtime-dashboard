package org.hanrw.app.akka;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Sink;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.hanrw.app.akka.config.CassandraProperties;
import org.hanrw.app.akka.config.KafkaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {

  @Autowired
  private KafkaProperties kafkaProperties;
  @Autowired
  private CassandraProperties cassandraProperties;

  private ActorSystem system = ActorSystem.create("KafkaClient");
  private ActorMaterializer materializer = ActorMaterializer.create(system);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(ApplicationArguments applicationArguments) {
    final ConsumerSettings<String, String> consumerSettings =
        ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
            .withBootstrapServers(kafkaProperties.getBootstrap())
            .withGroupId("group1")
            .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    Consumer.committableSource(consumerSettings, Subscriptions.topics(kafkaProperties.getTopic()))
        .map(msg -> {
          System.out.println(msg);
          return msg;
        })
        .runWith(Sink.ignore(), materializer);
  }

}

