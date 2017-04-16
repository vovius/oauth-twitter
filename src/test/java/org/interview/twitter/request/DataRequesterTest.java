package org.interview.twitter.request;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import org.interview.collect.MessageCollector;
import org.interview.model.Message;
import org.interview.model.User;
import org.interview.output.ConsolePrinter;
import org.interview.transform.MessageTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Volodymyr_Arseienko on 11.04.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpRequestFactory.class, HttpRequest.class, HttpResponse.class})
public class DataRequesterTest {

    private static final int RETRIEVE_PERIOD = 3;
    private static final int MAX_MESSAGES = 10;
    private DataRequester dataRequester;
    private Logger loggerDataRequester;
    private Logger loggerMessageCollector;
    private MessageCollector messageCollector = new MessageCollector();
    private MessageTransformer messageTransformer = new MessageTransformer();
    private ConsolePrinter consolePrinter = new ConsolePrinter();

    @Before
    public void init() throws IOException {
        dataRequester = new DataRequester("http://google.com");
        ReflectionTestUtils.setField(dataRequester, "retrievePeriod", RETRIEVE_PERIOD);
        ReflectionTestUtils.setField(dataRequester, "maxMessages", MAX_MESSAGES);
        dataRequester.setRequestFactory(mockRequestFactory());

        loggerDataRequester = mock(Logger.class);
        Whitebox.setInternalState(DataRequester.class, "LOG", loggerDataRequester);

        loggerMessageCollector = mock(Logger.class);
        Whitebox.setInternalState(MessageCollector.class, "LOG", loggerMessageCollector);

        ReflectionTestUtils.setField(messageCollector, "messageTransformer", messageTransformer);
    }

    private HttpRequestFactory mockRequestFactory() throws IOException {
        HttpRequestFactory requestFactory = mock(HttpRequestFactory.class);
        HttpResponse response = mock(HttpResponse.class);
        HttpRequest request = mock(HttpRequest.class);

        when(requestFactory.buildPostRequest(any(), any())).thenReturn(request);
        when(request.execute()).thenReturn(response);
        when(response.getContent()).thenReturn(getTestContentWithMessages());
        doNothing().when(response).disconnect();
        when(response.getStatusCode()).thenReturn(HttpStatusCodes.STATUS_CODE_OK);

        return requestFactory;
    }

    private InputStream getTestContentWithMessages() {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("responses.txt");
        return stream;
    }

    @Test
    public void testRequestGetMessages() throws IOException {
        List<String> jsonMessages = dataRequester.request();
        assertEquals(MAX_MESSAGES, jsonMessages.size());
        Mockito.verify(loggerDataRequester, times(1)).info(anyString());

        Map<User,Set<Message>> messages = messageCollector.collect(jsonMessages);
        assertEquals(MAX_MESSAGES, messages.size());
        Mockito.verify(loggerMessageCollector, times(1)).info(anyString());

        consolePrinter.print(messages);

    }

}
