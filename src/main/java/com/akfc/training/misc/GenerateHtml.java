package com.akfc.training.misc;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Objects;

public class GenerateHtml {

    public static void main(String[] args) {
        try {
            // Load the XML and XSLT files
            File xmlFile = new File(Objects.requireNonNull(GenerateHtml.class.getResource("/employees.xml")).getPath());
            File xsltFile = new File(Objects.requireNonNull(GenerateHtml.class.getResource("/employees.xslt")).getPath());

            // Create the TransformerFactory
            TransformerFactory factory = TransformerFactory.newInstance();

            // Create the transformer
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Perform the transformation
            transformer.transform(new StreamSource(xmlFile), new StreamResult(System.out));

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
