package fr.cenotelie.training.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class SearchFiles {

    private boolean searchText(String line, String regex) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(line).matches();
    }

    public void walkDirectory(String path, String extension, String regex, List<File> matches) throws IOException {
        File f = new File(path);
        if (! f.isDirectory()) {
            if (f.getName().endsWith(extension)) {
                if (Files.lines(f.toPath(), StandardCharsets.UTF_8).anyMatch(t -> searchText(t, regex))) {
                    matches.add(f);
                }
            }
        } else {
            for (File c : f.listFiles()) {
                walkDirectory(c.getAbsolutePath(), extension, regex, matches);
            }
        }
    }

    public void walkDir(String directory, String extension, String pattern) throws IOException {
        Files.walkFileTree(Paths.get(directory), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(extension) && Files.lines(file, StandardCharsets.UTF_8).anyMatch(t -> searchText(t, pattern))) {
                    System.out.println("Found matching file: " + file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void findFiles(String directory, String extension, String pattern) throws IOException {
        try (Stream<Path> pathStream = Files.find(Paths.get(directory), Integer.MAX_VALUE,
                (path, attr) -> {
                    try {
                        return path.toString().endsWith(extension) &&
                                Files.lines(path, StandardCharsets.UTF_8).anyMatch(t -> searchText(t, pattern));
                    } catch (IOException ex) {
                        return false;
                    }
                })) {
            pathStream.forEach(System.out::println);
        }
    }

    public static void main(String[] args) throws IOException {
        SearchFiles c = new SearchFiles();
        List<File> matches = new ArrayList<>();
        c.walkDirectory("/home/ali/tmp", ".json", "^\\[", matches);
        System.out.format("Found %d matching files", matches.size());
    }

}
