package spl.texteditor.storage;

import java.io.InputStream;
import java.io.OutputStream;

public interface TextStorageService {
    InputStream retrieve(String identifier);
    void store(String identifier, String content, boolean createIfMissing);
}
