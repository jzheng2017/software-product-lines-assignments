package spl.texteditor.storage;

/**
 * A service allowing you to read and write from/to a source
 */
public interface ReadWriteService {

    /**
     * Read from a specific source
     *
     * @param identifier a string that uniquely identifies the source
     * @return contents within the source
     */
    String read(String identifier);

    /**
     * Write to a specific source
     *
     * @param identifier a string that uniquely identifies the source
     * @param content    the text you want to write to the source
     */
    void write(String identifier, String content);

    String lastFileRead();

    String lastFileWritten();
}
