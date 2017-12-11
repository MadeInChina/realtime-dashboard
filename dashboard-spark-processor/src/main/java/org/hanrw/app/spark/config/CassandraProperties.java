package org.hanrw.app.spark.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cassandra")
@Data
public class CassandraProperties {
    private String host;
    private String port;
    private String keyspace;
    private String new_user_table;
    private String keep_alive;
}
