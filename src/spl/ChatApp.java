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

        //#if Rot13
        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.ROT13));
        encryptionDecided = true;
        //#endif

        //#if Reverse
//@        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.REVERSE));
//@        encryptionDecided = true;
        //#endif

        if (!encryptionDecided) {
            chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.PLAIN));
        }
    }

    public static void main(String[] args) throws IOException {
        determineEncryptionMethod();
        //#if GUI
//@        new GUI(chatService);
        //#endif
        //#if Console
        new CLI(chatService);
        //#endif
    }


}
