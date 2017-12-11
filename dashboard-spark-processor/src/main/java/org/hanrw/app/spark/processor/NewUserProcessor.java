package org.hanrw.app.spark.processor;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.hanrw.app.spark.config.CassandraProperties;
import org.hanrw.app.spark.config.KafkaProperties;
import org.hanrw.app.spark.config.SparkProperties;
import org.hanrw.app.spark.util.NewUserDataDeserializer;
import org.hanrw.app.spark.vo.NewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class consumes Kafka NewUser messages and creates stream for processing the NewUser data.
 */
@Service
@Slf4j
public class NewUserProcessor {
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private CassandraProperties cassandraProperties;
    @Autowired
    private SparkProperties sparkProperties;


    public void start() throws Exception {
        JavaStreamingContext jssc = create();
        jssc.start();
        jssc.awaitTermination();
    }


    public JavaStreamingContext create() throws Exception {
        //read Spark and Cassandra properties and create SparkConf
        SparkConf conf = new SparkConf()
                .setAppName(sparkProperties.getApp_name())
                .setMaster(sparkProperties.getMaster())
                .set("spark.cassandra.connection.host", cassandraProperties.getHost())
                .set("spark.cassandra.connection.port", cassandraProperties.getPort())
                .set("spark.cassandra.connection.keep_alive_ms", cassandraProperties.getKeep_alive());
        //batch interval of 5 seconds for incoming stream
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        //add check point directory
        jssc.checkpoint(sparkProperties.getCheckpoint());

        //read and set Kafka properties
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", kafkaProperties.getBootstrap());
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", NewUserDataDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        String topic = kafkaProperties.getTopic();
        Collection<String> topics = Lists.newArrayList();
        topics.add(topic);
        //create direct kafka stream
        final JavaInputDStream<ConsumerRecord<String, NewUser>> directKafkaStream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, NewUser>Subscribe(topics, kafkaParams)
                );

        log.info("Starting Stream Processing");

        //We need non filtered stream for new user data calculation
        JavaDStream<NewUser> nonFilteredIotDataStream = directKafkaStream.map(cr -> {
                    log.info("===================================== {}", cr.value().getCreatedTime());
                    return cr.value();
                }
        );
        //process data
        NewUserDataProcessor iotTrafficProcessor = new NewUserDataProcessor();
        iotTrafficProcessor.processTotalNewUserData(nonFilteredIotDataStream, cassandraProperties.getKeyspace(), cassandraProperties.getNew_user_table());
        return jssc;
    }
}
