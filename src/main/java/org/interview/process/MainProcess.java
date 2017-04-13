package org.interview.process;

import com.google.api.client.http.HttpRequestFactory;
import org.interview.model.Message;
import org.interview.model.User;
import org.interview.sort.MessageComparator;
import org.interview.transform.MessageTransformer;
import org.interview.sort.UserComparator;
import org.interview.twitter.oauth.TwitterAuthenticationException;
import org.interview.twitter.oauth.TwitterAuthenticator;
import org.interview.twitter.request.DataRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

/**
 * Created by sony on 4/9/2017.
 */
@Component
public final class MainProcess {
    private final static Logger LOG = LoggerFactory.getLogger(MainProcess.class);

    @Autowired
    private TwitterAuthenticator twitterAuthenticator;

    @Autowired
    private DataRequester requester;

    public void run() {
        LOG.info("Authorizing through OAuth...");

        HttpRequestFactory httpRequestFactory;
        try {
            httpRequestFactory = twitterAuthenticator.getAuthorizedHttpRequestFactory();
            LOG.info("Authorized");
        } catch (TwitterAuthenticationException e) {
            LOG.error("Authorization failed", e);
            return;
        }

        requester.setRequestFactory(httpRequestFactory);
        List<String> jsonMessages = requester.request();
        LOG.info(String.format("%d messages have been read", jsonMessages.size()));

        if (jsonMessages.isEmpty())
            return;

        Map<User,Set<Message>> messages = getConvertedMessages(jsonMessages);

    }

    private Map<User,Set<Message>> getConvertedMessages(List<String> jsonMessages) {
        Map<User,Set<Message>> messages = jsonMessages.stream()
                .map(MessageTransformer::fromJsonMessage)
                .collect(groupingBy(
                        Message::getAuthor,
                        () -> new TreeMap(new UserComparator()),
                        toCollection(
                                () -> new TreeSet<>(new MessageComparator())
                        )
                ));
        return messages;
    }
}
