package org.interview.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interview.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Volodymyr_Arseienko on 12.04.2017.
 */
@Component
public class MessageTransformer {
    private final static Logger LOG = LoggerFactory.getLogger(MessageTransformer.class);

    private static ObjectMapper objectMapper;

    public MessageTransformer() {
        objectMapper = new ObjectMapper();
        // Mon Apr 10 13:35:34 +0000 2017
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
        objectMapper.setDateFormat(dateFormat);
    }

    public Message fromJsonMessage(String jsonMessage) {
        Message message = null;
        try {
            message = objectMapper.readValue(jsonMessage, Message.class);
        } catch (IOException e) {
            LOG.error("Cannot transform string to Message: " + jsonMessage, e);
        }

        return message;
    }
}
