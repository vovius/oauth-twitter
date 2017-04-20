package org.interview.transform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

    private Gson gson;

    public MessageTransformer() {
        // Mon Apr 10 13:35:34 +0000 2017
        gson = new GsonBuilder()
                    .setDateFormat("EEE MMM d HH:mm:ss Z yyyy")
                    .create();
    }

    public Message fromJsonMessage(String jsonMessage) {
        Message message = null;
        try {
            message = gson.fromJson(jsonMessage, Message.class);
        } catch (JsonSyntaxException e) {
            LOG.error("Cannot transform string to Message: " + jsonMessage, e);
        }

        return message;
    }
}
