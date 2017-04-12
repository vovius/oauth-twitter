package org.interview.process.main;

import com.google.api.client.http.HttpRequestFactory;
import org.interview.model.Message;
import org.interview.process.transform.MessageTransformer;
import org.interview.twitter.oauth.TwitterAuthenticationException;
import org.interview.twitter.oauth.TwitterAuthenticator;
import org.interview.twitter.request.DataRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Message> messages = jsonMessages.stream()
                .map(MessageTransformer::convertToMessage)
                .collect(Collectors.toList());


    }
}
