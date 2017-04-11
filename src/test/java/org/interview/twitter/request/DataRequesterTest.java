package org.interview.twitter.request;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.crypto.Data;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Volodymyr_Arseienko on 11.04.2017.
 */
@RunWith(PowerMockRunner.class)
public class DataRequesterTest {

    private static final int RETRIEVE_PERIOD = 30;
    private static final int MAX_MESSAGES = 10;
    private DataRequester dataRequester;

    @Before
    public void init() {
        dataRequester = mock(DataRequester.class);
        ReflectionTestUtils.setField(dataRequester, "retrievePeriod", RETRIEVE_PERIOD);
        ReflectionTestUtils.setField(dataRequester, "maxMessages", MAX_MESSAGES);
    }

}
