package spl.texteditor.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

class FileTextStorageService implements TextStorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTextStorageService.class);

    @Override
    public InputStream retrieve(String identifier) {
        try {
            LOGGER.info("Retrieving contents of file '{}'", identifier);
            return new FileInputStream(identifier);
        } catch (FileNotFoundException ex) {
            LOGGER.warn("Could not find file with identifier/path '{}'", identifier);
            throw new IllegalArgumentException("File not found " + identifier);
        }
    }

    @Override
    public void store(String identifier, String content, boolean createIfMissing) {
        try {
            writingToFile(identifier, content);
        } catch (FileNotFoundException e) {
            LOGGER.warn("File could not be found with the provided file name '{}'.", identifier);
            if (createIfMissing) {
                createAndWriteToFile(identifier, content);
            }
        } catch (IOException ex) {
            LOGGER.warn("Something went wrong while trying to store the file '{}'. Please try again.", identifier);
        }
    }

    private void createAndWriteToFile(String identifier, String content) {
        try {
            File file = new File(identifier);
            LOGGER.info("Creating a new file..");
            if (file.createNewFile()) {
                LOGGER.info("New file created.");
                writingToFile(identifier, content);
            }
        } catch (IOException ex) {
            LOGGER.warn("Something went wrong trying to write to newly created file..", ex);
        }
    }

    private void writingToFile(String identifier, String content) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(identifier);
        try {
            LOGGER.info("Writing to file '{}'", identifier);
            fileOutputStream.write(content.getBytes());
        } finally {
            fileOutputStream.close();
        }
    }
}
