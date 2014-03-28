package releasecut.dependency;

import org.apache.maven.model.Model;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * tests of the DependencyService interface.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        DependencyServiceImpl.class,
        RemoteRepository.class,
        ArtifactRequest.class,
        ArtifactResult.class,
        DefaultRepositorySystemSession.class,
        MavenRepositorySystemUtils.class,
        DefaultServiceLocator.class,
        LocalRepository.class
})
public class TestDependencyService {
    @Mock
    RemoteRepository remoteRepository;

    @Mock
    RepositorySystem repositorySystem;

    @Mock
    RemoteRepository.Builder builder;

    @Mock
    File pomFile;

    @Mock
    ArtifactRequest artifactRequest;

    @Mock
    ArtifactResult artifactResult;

    @Mock
    DefaultRepositorySystemSession session;

    @Mock
    Artifact artifact;

    @Mock
    DefaultServiceLocator locator;

    @Mock
    LocalRepository localRepository;

    @Mock
    LocalRepositoryManager localRepositoryManager;

    @Test
    /**
     * Get a pom from the artifact repository
     */
    public void testGetPom() throws Exception {
        String url = "http://artifact.repo.url";
        mockStatic(RemoteRepository.Builder.class);
        mockStatic(MavenRepositorySystemUtils.class);
        when(MavenRepositorySystemUtils.newServiceLocator()).thenReturn(locator);
        when(MavenRepositorySystemUtils.newSession()).thenReturn(session);
        when(locator.getService(RepositorySystem.class)).thenReturn(repositorySystem);
        whenNew(RemoteRepository.Builder.class).withArguments("remoteRepository", "default", url).thenReturn(builder);
        whenNew(ArtifactRequest.class).withNoArguments().thenReturn(artifactRequest);
        whenNew(LocalRepository.class).withArguments("target/local-repo").thenReturn(localRepository);
        when(repositorySystem.resolveArtifact(session, artifactRequest)).thenReturn(artifactResult);
        when(artifactResult.getArtifact()).thenReturn(artifact);

        Model pom = new Model();
        pom.setArtifactId("aether-util");
        pom.setGroupId("org.eclipse.aether");
        pom.setVersion("0.9.0.v20140226");
        when(artifact.getFile()).thenReturn(new File("target\\local-repo\\org\\eclipse\\aether\\aether-util\\0.9.0.v20140226\\aether-util-0.9.0.v20140226.pom"));
        when(repositorySystem.newLocalRepositoryManager(session,localRepository)).thenReturn(localRepositoryManager);
        DependencyService dependencyService = new DependencyServiceImpl();
        dependencyService.start(url);
        Model pom2 = dependencyService.getPom("org.eclipse.aether","aether-util","0.9.0.v20140226");
        assertEquals(pom.getArtifactId(),pom2.getArtifactId());
        assertEquals(pom.getGroupId(),pom2.getGroupId());
        assertEquals(pom.getVersion(),pom2.getVersion());
    }

    public void testGetDependentPoms() throws DependencyCollectionException, XmlPullParserException, ArtifactResolutionException, IOException {
        String url = "http://artifact.repo.url";
        DependencyService dependencyService = new DependencyServiceImpl();
        dependencyService.start(url);


        List<Model> poms = dependencyService.getDependentPoms("org.eclipse.aether", "aether-util", "0.9.0.v20140226");



    }

}
