package releasecut.buildserver;

import releasecut.rest.RESTClientException;

/**
 * Created by ycv6026 on 3/24/14.
 */
public interface BuildServer {
    public String getUrl();

    public boolean isActive() throws RESTClientException;
}
