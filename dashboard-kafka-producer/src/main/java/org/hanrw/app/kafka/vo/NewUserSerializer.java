package org.hanrw.app.kafka.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Class to represent the IoT vehicle data.
 *
 * @author abaghel
 */
public class NewUserSerializer implements Serializer<NewUser> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {

    }

    @Override
    public byte[] serialize(String arg0, NewUser arg1) {
        byte[] retVal = null;
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}