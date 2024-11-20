package src;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Parsing class to extract headers, body, and path
public class Http_Mixin {
    private URI url;
    private String rawUrl;
    private String body;
    private Map<String, String> headers;

    public Http_Mixin() {
        this.headers = new HashMap<>();  // Ensures headers is not null
    }
    
    public void onBody(byte[] bodyBytes) {
        this.body = new String(bodyBytes, StandardCharsets.UTF_8);
    }
    
    public void onUrl(byte[] urlBytes) throws URISyntaxException {
        this.rawUrl = new String(urlBytes, StandardCharsets.UTF_8);
        this.url = new URI(this.rawUrl);
    }

    // ???
    public void onMessageComplete() {
        System.out.println("Received request to " + this.rawUrl);
    }

    public void onHeader(byte[] headerBytes, byte[] valueBytes) {
        String header = new String(headerBytes, StandardCharsets.UTF_8);
        String value = new String(valueBytes, StandardCharsets.UTF_8);
        this.headers.put(header, value);
    }

    // Getters
    public URI getUrl() {
        return url;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
