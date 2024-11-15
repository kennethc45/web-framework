package src;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Request class to group together HTTP request info
public class Request {
    private String version;
    private String encoding;
    private String method;
    private URI url;
    private Map<String, String> headers;
    private String body;
    private Object app;   // Object is a generic type
    private Map<String, String> matchInfo;
    private Map<String, String> queryParams;
    private String rawUrl;

    public Request(byte[] methodBytes, byte[] urlBytes, Map<String, String> headers, String version, byte[] bodyBytes, Object app) throws URISyntaxException {
        // If no version is provided, then the parameter will be null
        this.version = (version != null) ? version : null;
        this.encoding = "utf-8";
        // Decodes the byte arrays and then convert to string
        this.method = new String(methodBytes, StandardCharsets.UTF_8);
        this.rawUrl = new String(urlBytes, StandardCharsets.UTF_8);
        this.url = new URI(this.rawUrl);    // Java has no URL method for parsing URLs. URI is the closest
        this.headers = headers;
        // If no body is provided, then the parameter will be null
        this.body = (bodyBytes != null) ? new String(bodyBytes, StandardCharsets.UTF_8) : null;
        // If no app is provided, then the parameter will be null
        this.app = (app != null) ? app : null;
        this.matchInfo = new HashMap<>();
        this.queryParams = new HashMap<>();
        // Populates the queryParams from the URl
        parseQueryParams();
    }

    // Getter Methods
    public Object getApp() {
        return app;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getMatchInfo() {
        return matchInfo;
    }

    public String getMethod() {
        return method;
    }

    public URI getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getText() {
        return body;
    }

    // Setter Methods
    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setMatchInfo(Map<String, String> matchInfo) {
        this.matchInfo = matchInfo;
    }

    // Parase query parameters from the URL
    // https://example.com/path?name=Branch&products=[Universal%20Ads]
    // Query params come after "?"
    // Key value pairs are separated by "&"
    // Ex: name=Branch  key - name; value - Branch
    public void parseQueryParams() {
        // Retrieve query component of URL
        String query = url.getQuery();
        // Check if URL's query can be found
        if (query != null && !query.isEmpty()) {
            // Split the query into key value pairs
            String[] pairs = query.split("&");
            // Iterage though each pair
            for (String pair : pairs) {
                // Split each pair into key and value
                String[] keyValue = pair.split("=");
                if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                } else if (keyValue.length == 2) {
                    // Insert a map entry for the query param key and value
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    // Parses the body as JSON
    public Map<String, Object> getJson() throws JsonProcessingException {
        if (body != null) {
            // Provides functionality for reading and writing JSON
            ObjectMapper objectMapper = new ObjectMapper(); 
            // Parses JSON into Java object and then return it
            // TypeReference<> specifies the type to deserialize the JSON to
            return objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
        }
        return null;
    }

    // Returns unique ID/memory address for a request
    @Override
    public String toString() {
        // System.identityHashCode() returns the hash code of the request
        // Integer.toHexString() returns the string representation of the hash code 
        return "<Request at " + Integer.toHexString(System.identityHashCode(this)) +">";
    }
}
