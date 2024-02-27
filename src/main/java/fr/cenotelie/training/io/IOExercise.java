package fr.cenotelie.training.io;

import java.net.MalformedURLException;
import java.net.URL;

public class IOExercise {

    public static void main(String[] args) {
        try {
            var url = new URL("http://xmlns.com/foaf/spec/20140114.rdf");
            //TODO: read file and store it into a local file using a buffered input strean and Files API
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
