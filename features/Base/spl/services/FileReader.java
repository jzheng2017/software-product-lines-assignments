package spl.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    private static final Logger logger = Logger.getLogger(FileReader.class.getName());

    public static List<String> readAll(String path) {
        Path filePath = Path.of(path);

        try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(filePath)), StandardCharsets.UTF_8)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not retrieve the file contents", e);
        }

        return new ArrayList<>();
    }
}
