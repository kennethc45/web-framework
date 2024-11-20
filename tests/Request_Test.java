package tests;

import src.Request;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
        byte[] methodBytes = "GET".getBytes(StandardCharsets.UTF_8);
        byte[] urlBytes = "http://localhost:8080/path/?name=Joe&Age=25".getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";
        byte[] bodyBytes = "Some random body content".getBytes(StandardCharsets.UTF_8);
        Object app = new Object();

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, bodyBytes, app);
        boolean testPass = true;

        try {
            // Test Getter Methods
            assert request.getMethod().equals("GET") : "Expected Method 'GET'";
            assert request.getUrl().toString().equals("http://localhost:8080/path/?name=Joe&Age=25") : "URL Mismatch";
            assert request.getHeaders().size() == 2;
            assert request.getText().equals("Some random body content") : "Body mismatch";
        } catch (AssertionError e) {
            testPass = false;
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (testPass) {
            System.out.println("Request Initialization Test Passed");
        }
    }

    public static void testParseQueryParams() throws URISyntaxException {
        // Input Data
        byte[] methodBytes = "GET".getBytes();
        byte[] urlBytes = "http://localhost:8080/path/?city=NewYork&Country=UnitedStates".getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, null, null);
        Map<String, String> queryParams = request.getQueryParams();
        boolean testPass = true;

        try {
            // Check query paramaters
            assert queryParams.containsKey("city") : "Expected query param 'city'";
            assert queryParams.containsValue("NewYork") : "Expected NewYork for 'city' query param";
            assert queryParams.containsKey("Country") : "Expected query param 'Country'";
            assert queryParams.containsValue("UnitedStates") : "Expected UnitedStates for 'Country' query param";
        } catch (AssertionError e) {
            testPass = false;
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (testPass) {
            System.out.println("ParseQueryParams Test Passed");     
        }
    }

    public static void testGetJson() throws URISyntaxException, JsonProcessingException {
        byte[] methodBytes = "POST".getBytes();
        byte[] urlBytes = "http://localhost:8080/path".getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>() ; 
        headers.put("Host", "localhost");
        headers.put("Connection", "close");
        String version = "HTTP/1.1";
        String jsonBody = "{\"name\":\"John\",\"age\":\"30\"}";
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);

        // Create Request Object
        Request request = new Request(methodBytes, urlBytes, headers, version, bodyBytes, null);
        Map<String, Object> jsonMap = request.getJson();
        boolean testPass = true;

        try {
            assert jsonMap != null : "JSON shouldn't be null";
            assert jsonMap.get("name").equals("John") : "Expected 'John' for 'name' in JSON";
            assert jsonMap.get("age").equals("30") : "Expected '30' for 'age' in JSON";
        } catch (AssertionError e) {
            testPass = false;
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (testPass) {
            System.out.println("GetJson Test Passed");
        }
    }
}
