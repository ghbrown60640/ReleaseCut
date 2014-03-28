package releasecut;

import org.apache.maven.model.Model;
import releasecut.buildserver.BuildServer;
import releasecut.dependency.DependencyService;
import releasecut.rest.RESTClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ycv6026 on 3/24/14.
 */
public class ReleaseCutServiceImpl implements ReleaseCutService {
    private BuildServer buildServer;

    private DependencyService dependencyService;

    @Override
    public void addBuildServer(BuildServer buildServer) throws RESTClientException {
        this.buildServer = buildServer;
        buildServer.isActive();
    }

    public void addDependencyService(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }


    /**
     * Gets a list of {@link org.apache.maven.model.Model} objects that are all of the dependencies
     * and the
     *
     * @param coordinates
     * @return
     */
    @Override
    public List<Model> getPomAndDependentPoms(String coordinates) {
        return null;
    }

}
