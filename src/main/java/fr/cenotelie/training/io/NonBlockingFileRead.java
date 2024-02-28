package fr.cenotelie.training.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NonBlockingFileRead {

    public static void main(String[] args) {
        String path = NonBlockingFileRead.class.getResource("/lorem.txt").getPath();
        Path filePath = Paths.get(path);
        //TODO: read and display lines asynchronously
        //TODO: use classes from java.nio.file and java.util.concurrent packages
    }
}
