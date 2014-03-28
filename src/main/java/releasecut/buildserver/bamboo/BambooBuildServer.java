package releasecut.buildserver.bamboo;


import releasecut.buildserver.BuildServer;
import releasecut.rest.RESTClient;
import releasecut.rest.RESTClientException;

import java.util.Map;

/**
 * Class for accessing the API of a Bamboo server
 */
public class BambooBuildServer implements BuildServer {
    private final String url;

    public BambooBuildServer(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    private RESTClient makeRESTClient() {
        return  new RESTClient();
    }


    @Override
    public boolean isActive() throws  RESTClientException {
        String subpath="server";
        RESTClient restClient = makeRESTClient();
        Map<String,String> map = restClient.JSONGet(url + subpath);
        return map.get("state").equals("RUNNING");
    }


}
