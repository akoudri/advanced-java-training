package fr.cenotelie.training.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SearchFiles {

    private boolean searchText(String line, String regex) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(line).matches();
    }

    public void walkDirectory(String path, String extension, String regex, List<File> matches) throws IOException {
        //Walk through the arborescence to get matchin files
    }

    public static void main(String[] args) throws IOException {
        SearchFiles c = new SearchFiles();
        List<File> matches = new ArrayList<>();
        c.walkDirectory("/home/ali/tmp", ".json", "^\\[", matches);
        System.out.format("Found %d matching files", matches.size());
    }

}
