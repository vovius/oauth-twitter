package org.interview.output;

import org.interview.model.Message;
import org.interview.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by sony on 4/16/2017.
 */
@Component
public class ConsolePrinter {

    private final static Logger LOG = LoggerFactory.getLogger(ConsolePrinter.class);

    public void print(Map<User,Set<Message>> messages) {
        messages.entrySet().stream().forEach(entry -> {
            System.out.println(String.format("Printing %d cessages of user %s", entry.getValue().size(), entry.getKey()));
            entry.getValue().stream().forEach(System.out::println);
            System.out.println();
        });
    }
}
