package org.interview.collect;

import org.interview.model.Message;
import org.interview.model.User;
import org.interview.transform.MessageTransformer;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by sony on 4/18/2017.
 */
public class MessageCollectorTest {

    @Test
    public void testCollect() throws Exception {

        List<String> jsonMessages = getJsonMessages();
        MessageCollector messageCollector = new MessageCollector();
        MessageTransformer messageTransformer = new MessageTransformer();
        ReflectionTestUtils.setField(messageCollector, "messageTransformer", messageTransformer);
        Map<User, Set<Message>> result = messageCollector.collect(jsonMessages);
        assertEquals(2, result.keySet().size());

        assertTrue(result.values().stream().map(Set::size).collect(Collectors.toSet()).containsAll(Arrays.asList(1,2)));
    }

    private List<String> getJsonMessages() {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("responses_collectorTest.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<String> jsonMessages = new LinkedList<>();
        String line = null;
        do {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line != null)
                jsonMessages.add(line);
        } while (line != null);

        return jsonMessages;
    }

}