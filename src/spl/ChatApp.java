package spl;

import spl.services.ChatService;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import java.io.IOException;


public class ChatApp {

    private static ChatService chatService;

    private static void determineEncryptionMethod() {
        boolean encryptionDecided = false;

        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.ROT13));
        encryptionDecided = true;

        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.REVERSE));
        encryptionDecided = true;

        if (!encryptionDecided) {
            chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.PLAIN));
        }
    }

    public static void main(String[] args) throws IOException {
        determineEncryptionMethod();
        new GUI(chatService);
        new CLI(chatService);
    }


}
