package org.hanrw.app.spark.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.consumer")
@Data
public class KafkaProperties {
    private String bootstrap;
    private String topic;
}
