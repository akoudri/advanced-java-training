package com.akfc.training.network;

import com.akfc.training.misc.GenerateHtml;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

        //TODO: request with following header: "Accept": application/sparql-results+xml
        //TODO: use response body to generate a html page
    }

    private static void generateHtmlFile(String xmlResponse) throws Exception {
        // XSLT stylesheet to transform SPARQL XML results to HTML table
        File xsltFile = new File(Objects.requireNonNull(GenerateHtml.class.getResource("/beatles.xslt")).getPath());

        //TODO: parse and generate
    }
}
