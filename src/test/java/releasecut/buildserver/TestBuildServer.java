package releasecut.buildserver;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import releasecut.buildserver.bamboo.BambooBuildServer;
import releasecut.rest.RESTClient;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Tests for the @BuildServer interface
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BambooBuildServer.class)
public class TestBuildServer {
    @Mock
    RESTClient restClient;

    @Test
    public void testIsActive() {
        String url = "http://bamboo.url.com";
        BuildServer buildServer = PowerMockito.spy(new BambooBuildServer(url));
        Map<String,String> map = new LinkedHashMap<String, String>();
        map.put("state","RUNNING");
        try {
            when(buildServer,method(BambooBuildServer.class,"makeRESTClient")).withNoArguments().thenReturn(restClient);
            when(restClient.JSONGet(url + "server")).thenReturn(map);
            assert(buildServer.isActive());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
