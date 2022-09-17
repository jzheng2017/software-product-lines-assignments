package spl.services;

import java.util.List;
import java.util.stream.Collectors;

public class ChatService {
    private final LogService logService;
    private final EncryptionService encryptionService;

    public ChatService(LogService logService, EncryptionService encryptionService) {
        this.logService = logService;
        this.encryptionService = encryptionService;
    }

    public void sendMessage(String message) {
        logService.write(encryptionService.encrypt(message));
    }

    public List<String> readAll() {
        return FileReader
                .readAll(FileConstants.FILE_NAME)
                .stream()
                .filter(line -> !line.isEmpty())
                .map(encryptionService::decrypt)
                .collect(Collectors.toList());
    }

    public void clearChatLogs() {
        logService.clear();
    }
}
