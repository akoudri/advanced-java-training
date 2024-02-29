package com.akfc.training.network;

public class Beatles {

    public static void main(String[] args) throws Exception {
        String query = """
                    SELECT DISTINCT ?name ?release WHERE {
                          wd:Q61027305 wdt:P527 ?album.
                      ?album rdfs:label ?name; wdt:P577 ?release.
                      filter(lang(?name) = "fr").
                        }
                """;
        //Perform the query on the following URL: https://query.wikidata.org/sparql
        //The query shall be passed as request parameter (named query)

        //Generate html file from response
    }
}
