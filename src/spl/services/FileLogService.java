package spl.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLogService implements LogService {
    private Logger logger = Logger.getLogger(FileLogService.class.getName());

    @Override
    public void write(String message) {
        try (FileOutputStream outputStream = new FileOutputStream(FileConstants.FILE_NAME, true)) {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not write to " + FileConstants.FILE_NAME, e);
        }
    }

    @Override
    public void clear() {
        try {
            new FileOutputStream(FileConstants.FILE_NAME).close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not clear the file contents", e);
        }
    }
}
