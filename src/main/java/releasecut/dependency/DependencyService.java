package releasecut.dependency;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.ArtifactResolutionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Service that handles getting dependency information from the artifact repository
 */
public interface DependencyService {
    /**
     * Takes a URL parameter that is the URL of the remote artifact repo.
     * @param url
     */
    void start(String url);

    /**
     * Returns a {@link org.apache.maven.model.Model} object of the pom file for a project retrieved from an artifact repository
     * @param groupId
     * @param artifactId
     * @param version
     */
    Model getPom(String groupId, String artifactId, String version) throws ArtifactResolutionException, IOException, XmlPullParserException;

    /**
     * Gets a {@link java.util.List} of {@link org.apache.maven.model.Model} records that make up all the dependencies
     * (Including the transitive dependencies) for a specific maven project.
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    List<Model> getDependentPoms(String groupId, String artifactId, String version) throws DependencyCollectionException, XmlPullParserException, IOException, ArtifactResolutionException;
}
