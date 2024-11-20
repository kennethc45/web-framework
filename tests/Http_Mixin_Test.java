package tests;

import java.net.URISyntaxException;
import java.util.HashMap;
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
        byte[] bodyBytes = "Some random body content".getBytes();
        byte[] headerBytes1 = "Content-Type".getBytes();
        byte[] valueBytes1 = "application/json".getBytes();
        byte[] headerBytes2 = "User-Agent".getBytes();
        byte[] valueBytes2 = "JavaHttpClient".getBytes();

        parser.onUrl(urlBytes);
        parser.onMessageComplete();
        parser.onBody(bodyBytes);
        parser.onHeader(headerBytes1, valueBytes1);
        parser.onHeader(headerBytes2, valueBytes2);

        assert parser.getUrl().toString().equals("http://localhost:8080/path/?name=Joe&Age=25") : "URL Mismatch";
        assert parser.getBody().toString().equals("Some random body content") : "Body Mismatch";
        
    }
}
