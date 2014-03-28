package releasecut.rest;

/**
 * Wrapper Exception for REST client issues.
 */
public class RESTClientException extends Exception {
    private String url;
    private String message;
    public RESTClientException() {
        super();
    }
    public RESTClientException(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getUrl() {
        return this.url;
    }
    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
