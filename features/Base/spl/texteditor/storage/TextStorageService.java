package spl.texteditor.storage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A service allowing you to retrieve and store from a particular source
 */
public interface TextStorageService {
    /**
     * Retrieving data from a specific source
     *
     * @param identifier the identifier that unique identifies the source
     * @return an inputstream allowing you to stream the source
     */
    InputStream retrieve(String identifier);

    /**
     * Storing data to the source
     *
     * @param identifier      the identifier that unique identifies the source
     * @param content         the content you want to store
     * @param createIfMissing a flag determining whether the source has to be created if it doesn't exist
     */
    void store(String identifier, String content, boolean createIfMissing);
}
