package tests;

import src.Request;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Request_Test {
    public static void main(String[] args) throws Exception {
        testRequestInitialization();
        testParseQueryParams();
        testGetJson();
    }
 
    public static void testRequestInitialization() throws URISyntaxException {
        // Input Data
        byte[] methodBytes = "GET".getBytes();
        byte[] urlBytes = "http://localhost:8080/path/?name=Joe&Age=25".getBytes();
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";
        byte[] bodyBytes = "Some random body content".getBytes();
        Object app = new Object();

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, bodyBytes, app);

        // Test Getter Methods
        assert request.getMethod().equals("GET") : "Expected Method 'GET'";
        assert request.getUrl().toString().equals("http://localhost:8080/path/?name=Joe&Age=25") : "URL Mismatch";
        assert request.getHeaders().size() == 2;
        assert request.getText().equals("Some random body content") : "Body mismatch";

        System.out.println("Request Initialization Test Passed");
    }

    public static void testParseQueryParams() throws URISyntaxException {
        // Input Data
        byte[] methodBytes = "GET".getBytes();
        byte[] urlBytes = "http://localhost:8080/path/?city=NewYork&Country=UnitedStates".getBytes();
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, null, null);

        // Check query paramaters
        Map<String, String> queryParams = request.getQueryParams();
        assert queryParams.containsKey("city") : "Expected query param 'city'";
        assert queryParams.containsValue("NewYork") : "Expected NewYork for 'city' query param";
        assert queryParams.containsKey("Country") : "Expected query param 'Country'";
        assert queryParams.containsValue("UnitedStates") : "Expected UnitedStates for 'Country' query param";

        System.out.println("ParseQueryParams Test Passed");
    }

    public static void testGetJson() throws URISyntaxException, JsonProcessingException {
        byte[] methodBytes = "POST".getBytes();
        byte[] urlBytes = "http://localhost:8080/path".getBytes();
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";
        String jsonBody = "{\"name\":\"John\",\"age\":\"30\"}";
        byte[] bodyBytes = jsonBody.getBytes();

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, bodyBytes, null);

        Map<String, Object> jsonMap = request.getJson();
        assert jsonMap != null : "JSON shouldn't be null";
        assert jsonMap.get("name").equals("John") : "Expected 'John' for 'name' in JSON";
        assert jsonMap.get("age").equals("30") : "Expected '30' for 'age' in JSON";

        System.out.println("GetJson Test Passed");
    }
}
