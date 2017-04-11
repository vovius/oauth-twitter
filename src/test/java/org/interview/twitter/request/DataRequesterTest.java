package org.interview.twitter.request;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.crypto.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Volodymyr_Arseienko on 11.04.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpRequestFactory.class, HttpRequest.class, HttpResponse.class})
public class DataRequesterTest {

    private static final int RETRIEVE_PERIOD = 30;
    private static final int MAX_MESSAGES = 10;
    private DataRequester dataRequester;

    @Before
    public void init() throws IOException {
        dataRequester = new DataRequester("http://google.com");
        ReflectionTestUtils.setField(dataRequester, "retrievePeriod", RETRIEVE_PERIOD);
        ReflectionTestUtils.setField(dataRequester, "maxMessages", MAX_MESSAGES);

        dataRequester.setRequestFactory(mockRequestFactory());

        Logger logger = mock(Logger.class);
        Whitebox.setInternalState(DataRequester.class, "LOG", logger);
    }

    private HttpRequestFactory mockRequestFactory() throws IOException {
        HttpRequestFactory requestFactory = mock(HttpRequestFactory.class);
        HttpResponse response = mock(HttpResponse.class);
        HttpRequest request = mock(HttpRequest.class);

        when(requestFactory.buildPostRequest(any(), any())).thenReturn(request);
        when(request.execute()).thenReturn(response);
        when(response.getContent()).thenReturn(getTestContent());
        doNothing().when(response).disconnect();
        when(response.getStatusCode()).thenReturn(HttpStatusCodes.STATUS_CODE_OK);

        return requestFactory;
    }

    private InputStream getTestContent() {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("response.txt");
        return stream;
    }

    @Test
    public void testRequest() {
        List<String> result = dataRequester.request();
        assertEquals(MAX_MESSAGES, result.size());
    }
}
