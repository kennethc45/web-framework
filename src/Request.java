package src;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        this.version = (version != null) ? version : null;
        this.encoding = "utf-8";
        this.method = new String(methodBytes, StandardCharsets.UTF_8);
        this.rawUrl = new String(urlBytes, StandardCharsets.UTF_8);
        this.url = new URI(this.rawUrl);    // Java has no URL method for parsing URLs. URI is the closest
        this.headers = headers;
        this.body = (bodyBytes != null) ? new String(bodyBytes, StandardCharsets.UTF_8) : null;
        this.app = (app != null) ? app : null;
        this.matchInfo = new HashMap<>();
        this.queryParams = new HashMap<>();
    }

    public Object getApp() {
        return app;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(Map<String, String> matchInfo) {
        this.matchInfo = matchInfo;
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

    // Parses the body as JSON
    public Map<String, Object> getJson() throws JsonProcessingException {
        if (body != null) {
            ObjectMapper objectMapper = new ObjectMapper(); 
            // return objectMapper.readValue(body, Map.class);
            return objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
        }
        return null;
    }
    
}
