package releasecut;

import org.apache.maven.model.Model;
import releasecut.buildserver.BuildServer;
import releasecut.dependency.DependencyService;
import releasecut.rest.RESTClientException;

import java.util.List;

/**
 * Created by ycv6026 on 3/24/14.
 */
public interface ReleaseCutService {

    /**
     * Sets the {@link releasecut.buildserver.BuildServer}
     * @param buildServer
     * @throws RESTClientException
     */
    void addBuildServer(BuildServer buildServer) throws RESTClientException;

    /**
     * Gets a list of {@link org.apache.maven.model.Model} objects that are all of the dependencies
     * and the
     * @return
     */
    List<Model> getPomAndDependentPoms(String coordinates);

    /**
     * sets the {@link releasecut.dependency.DependencyService} to enable retrieving dependencies from the artifact repo.
     * @param dependencyService
     */
    void addDependencyService(DependencyService dependencyService);
}
