package spl;

import spl.services.ChatService;
import spl.services.ColorService;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import spl.services.WithUsernameColorService;
import spl.services.WithoutUsernameColorService;

import java.io.IOException;


public class ChatApp {

    private static ChatService chatService;
    private static ColorService colorService;


    private static void determineEncryptionMethod(String encryptionType) {
        boolean encryptionDecided = false;
        
        if(encryptionType.equals("rot13"))
        {
        	chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.ROT13));
        	encryptionDecided = true;
        }
        else if(encryptionType.equals("reverse"))
        {
        	chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.REVERSE));
        	encryptionDecided = true;
        }
        if (!encryptionDecided) {
            chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.PLAIN));
        }
    }

    private static void useUsernameColor(boolean use) {
        colorService = use ? new WithUsernameColorService() : new WithoutUsernameColorService();
    }

    public static void main(String[] args) throws IOException {
        determineEncryptionMethod("rot13");
        useUsernameColor(true);
        new GUI(chatService, colorService);
        // new CLI(chatService, colorService);
    }


}
