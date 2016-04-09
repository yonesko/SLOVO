package util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class HttpConnectionAgent {

    public static HttpResponse connectResponse(URIBuilder uriBuilder) throws URISyntaxException, IOException {

        URI uri = null;

        uri = uriBuilder.build();

        HttpClient   client     = HttpClientBuilder.create().build();
        HttpGet      request    = new HttpGet(uri);
        HttpResponse response   = null;

        response = client.execute(request);

        return response;
    }
}