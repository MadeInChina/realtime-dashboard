package org.hanrw.app.spark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.hanrw.app.spark.vo.NewUser;

import java.util.Map;

/**
 * NewUserDataDeserializer
 */
public class NewUserDataDeserializer implements Deserializer<NewUser> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public NewUser deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, NewUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}