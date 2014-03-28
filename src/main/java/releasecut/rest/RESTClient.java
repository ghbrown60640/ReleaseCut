package releasecut.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper Class for REST and JSON functionality
 */
public class RESTClient {
    private String doGet(String url) throws RuntimeException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
        if (clientResponse.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + clientResponse.getStatus());
        }
        return clientResponse.getEntity(String.class);
    }

    public Map<String, String> JSONGet(String url) throws RESTClientException {
        Map<String, String> map;
        try {
            map = JSONToMap(doGet(url));
        } catch (Exception e) {
            RESTClientException exception = new RESTClientException(e.getMessage());
            exception.setUrl(url);
            throw exception;
        }
        return map;
    }

    private Map<String, String> JSONToMap(String jsonText) throws ParseException {
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap<String,String>();
            }

        };
        return (Map<String,String>) parser.parse(jsonText, containerFactory);

    }
}
