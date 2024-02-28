package fr.cenotelie.training.misc;

import java.io.File;

public class GenerateHtml {

    public static void main(String[] args) {
        // Load the XML and XSLT files
        File xmlFile = new File(GenerateHtml.class.getResource("/employees.xml").getPath());
        File xsltFile = new File(GenerateHtml.class.getResource("/employees.xslt").getPath());
        //TODO: use classes from javax.xml.transform package
        // Create the TransformerFactory

        // Create the transformer

        // Perform the transformation
    }

}
