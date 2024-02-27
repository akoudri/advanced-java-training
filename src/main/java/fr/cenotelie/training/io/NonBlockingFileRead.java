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

        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                filePath, StandardOpenOption.READ)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            System.out.println("Reading file content (non-blocking)...");
            long position = 0;

            while (true) {
                Future<Integer> readResult = fileChannel.read(buffer, position);
                while (!readResult.isDone()) {
                    // Perform other tasks or wait for data to become available
                    // without blocking the thread
                }

                int bytesRead = readResult.get();
                if (bytesRead == -1) {
                    break; // End of file reached
                }

                buffer.flip();
                byte[] data = new byte[bytesRead];
                buffer.get(data);
                String content = new String(data, StandardCharsets.UTF_8);
                System.out.print(content);

                buffer.clear();
                position += bytesRead;
            }

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
