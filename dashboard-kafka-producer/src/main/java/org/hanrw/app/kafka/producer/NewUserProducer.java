package org.hanrw.app.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.hanrw.app.kafka.vo.NewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * IoT data event producer class which uses Kafka producer for events.
 *
 * @author abaghel
 */
@Service
@Slf4j
public class NewUserProducer {
    @Autowired
    private KafkaTemplate<String, NewUser> registerUserKafkaTemplate;

    /**
     * @param topic
     * @throws InterruptedException
     */
    public void generateNewUsers(String topic) throws InterruptedException {
        List<String> users = Arrays.asList(new String[]{"1001", "1002", "1003"});
        // generate event in loop
        users.stream().forEach(r -> send(topic, r));
    }

    private void send(String topic, String userId) {
        try {
            NewUser newUser = new NewUser(userId, new Date());
            SendResult<String, NewUser> sendResult = registerUserKafkaTemplate.send(topic, newUser).get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
            log.info("topic = {}, partition = {}, offset = {}, workUnit = {}",
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
