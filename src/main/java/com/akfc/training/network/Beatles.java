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
        // Request XML format instead of JSON for XSLT processing
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).header("Accept", "application/sparql-results+xml").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("XML Response:");
        System.out.println(response.body());
        
        // Generate HTML file from XML response using XSLT
        generateHtmlFile(response.body());
    }
    
    /**
     * Generates an HTML file from the SPARQL XML response using XSLT transformation
     * @param xmlResponse The XML response from the SPARQL query
     * @throws Exception if transformation or file writing fails
     */
    private static void generateHtmlFile(String xmlResponse) throws Exception {
        // XSLT stylesheet to transform SPARQL XML results to HTML table
        File xsltFile = new File(Objects.requireNonNull(GenerateHtml.class.getResource("/beatles.xslt")).getPath());
        
        try {
            // Create transformer factory and transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
            
            // Prepare input and output streams
            StreamSource xmlSource = new StreamSource(new StringReader(xmlResponse));
            StringWriter htmlWriter = new StringWriter();
            StreamResult htmlResult = new StreamResult(htmlWriter);
            
            // Perform the transformation
            transformer.transform(xmlSource, htmlResult);
            
            // Write the HTML result to a file in the resources folder
            String htmlContent = htmlWriter.toString();
            String resourcesPath = new File("src/main/resources").getAbsolutePath();
            String fileName = resourcesPath + File.separator + "beatles_albums.html";
            
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(htmlContent);
            }
            
            System.out.println("\nHTML file generated successfully: " + fileName);
            System.out.println("You can open this file in your web browser to view the Beatles albums table.");
            
        } catch (Exception e) {
            System.err.println("Error generating HTML file: " + e.getMessage());
            throw e;
        }
    }
}
