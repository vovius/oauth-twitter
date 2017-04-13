package org.interview.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interview.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Volodymyr_Arseienko on 12.04.2017.
 */
public class MessageTransformer {
    private final static Logger LOG = LoggerFactory.getLogger(MessageTransformer.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Message fromJsonMessage(String jsonMessage) {
        Message message = null;
        try {
            message = objectMapper.readValue(jsonMessage, Message.class);
        } catch (IOException e) {
            LOG.error("Cannot transform string to Message: " + jsonMessage, e);
        }

        return message;
    }
}
