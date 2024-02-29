package com.akfc.training.network;

import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Beatles {

    public static void main(String[] args) throws Exception {
        String query = """
                    SELECT DISTINCT ?name ?release WHERE {
                          wd:Q61027305 wdt:P527 ?album.
                      ?album rdfs:label ?name; wdt:P577 ?release.
                      filter(lang(?name) = "fr").
                        }
                """;
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        URL url = new URL("https://query.wikidata.org/sparql?query=" + URLEncoder.encode(query, "UTF-8"));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).header("Accept", "application/xml").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        //TODO: generate html file from response
    }
}
