package fr.cenotelie.training.misc;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class GenerateHtml {

    public static void main(String[] args) {
        // Load the XML and XSLT files
        File xmlFile = new File(GenerateHtml.class.getResource("/employees.xml").getPath());
        File xsltFile = new File(GenerateHtml.class.getResource("/employees.xslt").getPath());

        // Create the TransformerFactory

        // Create the transformer

        // Perform the transformation
    }

}
