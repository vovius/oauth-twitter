package org.interview.twitter.request;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Volodymyr_Arseienko on 10.04.2017.
 */
@Component
public class DataRequester {

    private final String requestURL;
    private HttpRequestFactory requestFactory;

    public DataRequester(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestFactory(HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public void request() {
        GenericUrl url = new GenericUrl(requestURL);
        try {
            HttpRequest request = requestFactory.buildPostRequest(url, null);
            HttpResponse response = request.execute();

            String result = IOUtils.toString(response.getContent());
            System.out.println(result);
            response.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
