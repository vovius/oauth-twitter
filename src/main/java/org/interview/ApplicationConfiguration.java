package org.interview;

import org.interview.twitter.oauth.TwitterAuthenticator;
import org.interview.twitter.request.DataRequester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/**
 * Created by Volodymyr_Arseienko on 10.04.2017.
 */
@Configuration
@ComponentScan
@PropertySource("classpath:spring/application.properties")
public class ApplicationConfiguration {

    @Value("${twitter.key}") private String twitterKey;
    @Value("${twitter.secret}") private String twitterSecret;
    @Value("${trackURL}") private String trackURL;
    @Value("${trackOn}") private String trackOn;

    @Bean
    public TwitterAuthenticator twitterAuthenticator() {
        return new TwitterAuthenticator(System.out, twitterKey, twitterSecret);
    }

    @Bean
    public DataRequester dataRequester() {
        String requestURL = String.format(trackURL, trackOn);
        return new DataRequester(requestURL);
    }


}

