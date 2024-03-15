package com.akfc.training.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class DatabaseAccess {

    public static List<Movie> retreiveMovies(String firstname, String lastname) throws URISyntaxException, IOException, InterruptedException {
        List<Movie> movies = new ArrayList<>();
        //TODO: test this query on https://query.wikidata.org/
        String query = String.format("""
                    SELECT DISTINCT ?movieLabel ?movieDescription ?directorLabel ?genreLabel ?release WHERE {
                        SERVICE wikibase:label { bd:serviceParam wikibase:language "fr". }
                        {
                            SELECT DISTINCT ?movie ?director ?genre ?release WHERE {
                                ?movie wdt:P57 ?director; wdt:P136 ?genre; wdt:P577 ?release.
                                ?director wdt:P735 ?gn; wdt:P734 ?fn.
                                ?gn rdfs:label "%s"@en.
                                ?fn rdfs:label "%s"@en.
                            } LIMIT 500
                        }
                    }
                """, firstname, lastname);
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        URL url = new URL("https://query.wikidata.org/sparql?query=" + URLEncoder.encode(query, "UTF-8"));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).header("Accept", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject res = JsonParser.parseReader(new StringReader(response.body())).getAsJsonObject().getAsJsonObject("results");
        JsonArray bindings = res.getAsJsonArray("bindings");
        Pattern p = Pattern.compile("Q\\d+");
        for (int i = 0; i < bindings.size(); i++) {
            JsonObject obj = (JsonObject) bindings.get(i);
            String title = obj.getAsJsonObject("movieLabel").getAsJsonPrimitive("value").getAsString();
            if (p.matcher(title).matches()) continue;
            String director = obj.getAsJsonObject("directorLabel").getAsJsonPrimitive("value").getAsString();
            JsonObject d = obj.getAsJsonObject("movieDescription");
            String description = "No description";
            if (d != null)
                description = d.getAsJsonPrimitive("value").getAsString();
            String genre = obj.getAsJsonObject("genreLabel").getAsJsonPrimitive("value").getAsString();
            String release = obj.getAsJsonObject("release").getAsJsonPrimitive("value").getAsString();
            if (movies.stream().filter(e -> e.title().equalsIgnoreCase(title) && e.director().equalsIgnoreCase(director)).count() == 0)
                movies.add(new Movie(title, director, description, genre, release));
        }
        return movies;
    }

    public static void feedDatabase() throws IOException {
        Properties props = new Properties();
        props.load(DatabaseAccess.class.getResourceAsStream("/db.properties"));
        try (
                Connection conn = DriverManager.getConnection(props.getProperty("url"));
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS movies (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255),
                        director VARCHAR(255),
                        description CHARACTER LARGE OBJECT,
                        genre VARCHAR(255),
                        release VARCHAR(255),
                        CONSTRAINT u_cst_1 UNIQUE(title, director)
                    )
                    """);
            List<Movie> movies = new ArrayList<>();
            movies.addAll(retreiveMovies("Quentin", "Tarantino"));
            movies.addAll(retreiveMovies("Steven", "Spielberg"));
            movies.addAll(retreiveMovies("Wes", "Anderson"));
            movies.addAll(retreiveMovies("George", "Lucas"));
            movies.addAll(retreiveMovies("David", "Fincher"));
            for (Movie m : movies) {
                String query = String.format("""
                        INSERT INTO movies (title, director, description, genre, release)
                        VALUES ('%s', '%s', '%s', '%s', '%s')
                        """,
                        m.title().replace("'", "''"),
                        m.director(),
                        m.description().replace("'", "''"),
                        m.genre().replace("'", "''"),
                        m.release()
                );
                stmt.executeUpdate(query);
            }
        } catch (SQLException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        H2ConsoleThread h2Console = new H2ConsoleThread();
        h2Console.start();
        feedDatabase();
        System.out.println("Press Enter to exit...");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            reader.readLine();
            h2Console.join();
            System.out.println("Exiting the application...");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class H2ConsoleThread extends Thread {

        @Override
        public void run() {
            try {
                org.h2.tools.Console.main();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
