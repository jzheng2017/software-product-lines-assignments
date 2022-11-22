package spl.texteditor.storage;

public interface ReadWriteService {

    String read(String identifier);
    void write(String identifier, String content);

    String lastFileRead();
    String lastFileWritten();
}
