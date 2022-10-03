package spl.services;

import java.util.List;
import java.util.ArrayList;
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
    	List<String> lines = FileReader.readAll(FileConstants.FILE_NAME);
    	List<String> newLines = new ArrayList<String>();
    	
    	for (String line : lines) {
    		if (!line.isEmpty()) {
    			newLines.add(encryptionService.decrypt(line));
    		}
    	}
    	
        return newLines;
    }

    public void clearChatLogs() {
        logService.clear();
    }
}
