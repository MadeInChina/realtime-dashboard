package org.hanrw.app.spark.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spark")
@Data
public class SparkProperties {
    private String app_name;
    private String master;
    private String checkpoint;
}
