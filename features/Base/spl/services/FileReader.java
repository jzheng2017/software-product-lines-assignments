package spl.services;

import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    private static final Logger logger = Logger.getLogger(FileReader.class.getName());

    public static List<String> readAll(String path) {
        File file = new File(path);
        try {
        	Scanner input = new Scanner(file);
        	List<String> lines = new ArrayList<String>();
        	while (input.hasNextLine()) {
        	    lines.add(input.nextLine());
        	}
        	input.close();
            return lines;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not retrieve the file contents", e);
        }

        return new ArrayList<String>();
    }
}
