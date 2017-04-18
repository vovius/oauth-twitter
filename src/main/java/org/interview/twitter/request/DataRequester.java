package org.interview.twitter.request;

import com.google.api.client.http.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Volodymyr_Arseienko on 10.04.2017.
 */
public class DataRequester {

    private final static Logger LOG = LoggerFactory.getLogger(DataRequester.class);

    private final String requestURL;
    private HttpRequestFactory requestFactory;

    private int retrievePeriod;
    private int maxMessages;

    public DataRequester(String requestURL, int retrievePeriod, int maxMessages) {
        this.requestURL = requestURL;
        this.retrievePeriod = retrievePeriod;
        this.maxMessages = maxMessages;
    }


    public void setRequestFactory(HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public List<String> request() {
        GenericUrl url = new GenericUrl(requestURL);
        try {
            LOG.info("Reading messages...");

            HttpRequest request = requestFactory.buildPostRequest(url, null);
            final HttpResponse response = request.execute();

            if (response.getStatusCode() == HttpStatusCodes.STATUS_CODE_OK) {
                List<String> resultMessages = new LinkedList<>();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                final InputStream inputStream = response.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                long startTime = System.currentTimeMillis();
                final Future task = executorService.submit(() -> {
                    int curCnt = 0;
                    while (curCnt < maxMessages) {
                        String line = null;
                        try {
                            line = reader.readLine();
                        } catch (IOException e) {
                            LOG.error("Error when reading message, curCnt=" + curCnt);
                            break;
                        }

                        if (line != null) {
                            resultMessages.add(line);
                            curCnt++;
                        } else
                            break;

                        LOG.debug(line);
                    }

                });

                try {
                    task.get(retrievePeriod, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    LOG.debug("Interrupted!", e);
                } catch (ExecutionException e) {
                    LOG.error("Exception during execution: ", e);
                } catch (TimeoutException e) {
                    LOG.info("Expired by timeout, messages read: " + resultMessages.size());
                }

                long endTime = System.currentTimeMillis();

                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("Error when closing reader: ", e);
                }

                executorService.shutdown();

                String formattedTime = formattedExecutionTime(startTime, endTime);

                LOG.info(String.format("%d messages have been read, time=%s", resultMessages.size(), formattedTime));
                return resultMessages;

            } else {
                LOG.error("Failed to create Twitter streaming connection, error code = " + response.getStatusCode());
            }

            response.disconnect();
        } catch (IOException e) {
            LOG.error("Failed to execute request to Twitter: ", e);
        }

        return null;

    }

    private String formattedExecutionTime(long startTime, long endTime) {
        long duration = endTime - startTime;
        long seconds = duration / 1000;
        long millis = duration % 1000;
        return String.format("%d.%3d sec", seconds, millis);
    }

}
