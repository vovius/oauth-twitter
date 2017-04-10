package org.interview;

import org.interview.twitter.oauth.TwitterAuthenticator;
import org.interview.process.MainProcess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

/**
 * Created by sony on 4/9/2017.
 */
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        MainProcess mainProcess = context.getBean(MainProcess.class);
        mainProcess.run();
    }
}
