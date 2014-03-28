package releasecut.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Test for the JSONUtils Class
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RESTClient.class)
public class TestRESTClient {

    @Test
    public void testJSONGet() {
        RESTClient restClient = PowerMockito.spy(new RESTClient());
        Map<String, String> m2;

        try {
            when(restClient, method(RESTClient.class, "doGet", String.class)).withArguments("http://good.url.com").thenReturn("{\"state\":\"RUNNING\"}");
            m2 = restClient.JSONGet("http://good.url.com");
            assertEquals(m2.get("state"), "RUNNING");
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
