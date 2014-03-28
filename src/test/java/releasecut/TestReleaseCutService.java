package releasecut;



import org.apache.maven.model.Model;
import org.eclipse.aether.repository.RemoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import releasecut.buildserver.BuildServer;

import releasecut.rest.RESTClientException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the ReleaseCutService class
 */
@RunWith(MockitoJUnitRunner.class)
public class TestReleaseCutService {

    @Mock
    BuildServer buildServer;


    @Test
    public void testServiceSetup() {
        ReleaseCutService releaseCutService = new ReleaseCutServiceImpl();
        String coord = "com.foo.bar:main-jar:1.0-SNAPSHOT";
        List<Model> poms=releaseCutService.getPomAndDependentPoms(coord);
        assertEquals(poms.size(),1 );
        try {
            when(buildServer.isActive()).thenReturn(true);
            releaseCutService.addBuildServer(buildServer);
            verify(buildServer).isActive();
        } catch (RESTClientException e) {
            e.printStackTrace();
        }
    }


}
