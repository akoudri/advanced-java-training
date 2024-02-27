package fr.cenotelie.training.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NonBlockingFileRead {

    public static void main(String[] args) {
        String path = NonBlockingFileRead.class.getResource("/lorem.txt").getPath();
        Path filePath = Paths.get(path);
        //TODO: read and display lines asynchronously
    }
}
