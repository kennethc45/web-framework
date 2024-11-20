package tests;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import src.Http_Mixin;

public class Http_Mixin_Test {
    public static void main(String[] args) throws Exception {
        testRequestParsing();
    }

    public static void testRequestParsing() throws URISyntaxException {
        Http_Mixin parser = new Http_Mixin();

        // Input Data
        byte[] urlBytes = "http://localhost:8080/path/?name=Joe&Age=25".getBytes();
        byte[] bodyBytes = "Some random body content".getBytes(StandardCharsets.UTF_8);
        byte[] headerBytes1 = "Content-Type".getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes1 = "application/json".getBytes(StandardCharsets.UTF_8);
        byte[] headerBytes2 = "User-Agent".getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes2 = "JavaHttpClient".getBytes(StandardCharsets.UTF_8);

        parser.onUrl(urlBytes);
        parser.onMessageComplete();
        parser.onBody(bodyBytes);
        parser.onHeader(headerBytes1, valueBytes1);
        parser.onHeader(headerBytes2, valueBytes2);

        Map<String, String> headers = parser.getHeaders();
        boolean testPass = true;

        try {
            assert parser.getUrl().toString().equals("http://localhost:8080/path/?name=Joe&Age=25") : "URL Mismatch";
            assert parser.getBody().equals("Some random body content") : "Body Mismatch";
            assert headers.get("Content-Type").equals("application/json") : "Header Content-Type Mismatch";
            assert headers.get("User-Agent").equals("JavaHttpClient") : "Header User-Agent Mismatch";
        } catch (AssertionError e) {
            testPass = false;
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (testPass) {
            System.out.println("Request Parsing Test Passed");
        }
        
    }
}
