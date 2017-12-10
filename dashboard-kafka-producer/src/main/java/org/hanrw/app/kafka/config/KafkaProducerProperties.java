package org.hanrw.app.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.producer")
@Data
public class KafkaProducerProperties {
    private String bootstrap;
    private String topic;
}
