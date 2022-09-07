package spl.services;

import java.util.List;

public class ChatService {
    private LogService logService;

    public ChatService(LogService logService) {
        this.logService = logService;
    }

    public void sendMessage(String message) {
        logService.write(message);
    }

    public List<String> readAll() {
        return FileReader.readAll(FileConstants.FILE_NAME);
    }

    public void clearChatLogs() {
        FileReader.clearText(FileConstants.FILE_NAME);
    }
}
