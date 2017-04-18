package org.interview.collect;

import org.interview.model.Message;
import org.interview.model.User;
import org.interview.sort.MessageComparator;
import org.interview.sort.UserComparator;
import org.interview.transform.MessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

/**
 * Created by sony on 4/16/2017.
 */
@Component
public class MessageCollector {

    private final static Logger LOG = LoggerFactory.getLogger(MessageCollector.class);

    @Autowired
    private MessageTransformer messageTransformer;

    public Map<User,Set<Message>> collect(List<String> jsonMessages) {
        Map<User,Set<Message>> messages = jsonMessages.stream()
                .map(message -> messageTransformer.fromJsonMessage(message))
                .filter(Message::notNull)
                .collect(groupingBy(
                        Message::getAuthor,
                        () -> new TreeMap<>(new UserComparator()),
                        mapping(Function.identity(),
                                toCollection(
                                        () -> new TreeSet<>(new MessageComparator())
                                )
                        )
                ));

        int messagesCnt = messages.values().stream().mapToInt(Set::size).sum();
        LOG.info(String.format("%d users with %d messages have been collected", messages.keySet().size(), messagesCnt));

        return messages;
    }

}
