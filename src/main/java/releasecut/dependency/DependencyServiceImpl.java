package releasecut.dependency;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Testing DependencyService class
 */
public class DependencyServiceImpl implements DependencyService {

    private RemoteRepository remoteRepository;
    private RepositorySystem repositorySystem;
    private DefaultRepositorySystemSession session;

    @Override
    /**
     * Sets up the Aether session
     */
    public void start(String url) {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, FileTransporterFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
        repositorySystem = locator.getService(RepositorySystem.class);
        remoteRepository = new RemoteRepository.Builder("remoteRepository","default",url).build();
        session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository( "target/local-repo" );
        LocalRepositoryManager localRepositoryManager = repositorySystem.newLocalRepositoryManager(session, localRepo);
        System.out.println(session.getChecksumPolicy());
        System.out.println(localRepo.getContentType());
        System.out.println(localRepositoryManager.toString());
        session.setLocalRepositoryManager(localRepositoryManager);
    }

    @Override
    /**
     * Returns a {@link org.apache.maven.model.Model} object of the pom file for a project retrieved from an artifact repository via Aether.
     * @param groupId
     * @param artifactId
     * @param version
     */
    public Model getPom(String groupId, String artifactId, String version) throws ArtifactResolutionException, IOException, XmlPullParserException {
        String pomstring = groupId + ":" + artifactId + ":pom:" + version;
        Artifact artifact = new DefaultArtifact(pomstring);
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact( artifact );
        List<RemoteRepository> repos = new ArrayList<RemoteRepository>();
        repos.add(remoteRepository);
        artifactRequest.setRepositories(repos);
        ArtifactResult artifactResult = repositorySystem.resolveArtifact( session, artifactRequest );

        artifact = artifactResult.getArtifact();

        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model pom = reader.read(new FileInputStream(artifact.getFile()));
        return pom;
    }

    /**
     * Gets a {@link java.util.List} of {@link org.apache.maven.model.Model} records that make up all the dependencies
     * (Including the transitive dependencies) for a specific maven project.
     *
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    @Override
    public List<Model> getDependentPoms(String groupId, String artifactId, String version) throws DependencyCollectionException, XmlPullParserException, IOException, ArtifactResolutionException {
        String coords = groupId + ":" + artifactId + ":" + version;
        Artifact artifact = new DefaultArtifact(coords);
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( new Dependency( artifact, "" ) );
        List<RemoteRepository> repos = new ArrayList<RemoteRepository>();
        repos.add(remoteRepository);
        collectRequest.setRepositories(repos);
        CollectResult collectResult = repositorySystem.collectDependencies(session,collectRequest);
        DependencyNode rootNode = collectResult.getRoot();
        Artifact rootArtifact = rootNode.getArtifact();
        Model rootPom = getPom(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
        Iterator<DependencyNode> dependencies = rootNode.getChildren().iterator();
        List<Model> poms = new ArrayList<Model>();

        while (dependencies.hasNext()) {
            Artifact a  = dependencies.next().getArtifact();
            Model m = getPom(a.getGroupId(),a.getArtifactId(),a.getVersion());
            poms.add(m);

        }
        return poms;
    }


}
