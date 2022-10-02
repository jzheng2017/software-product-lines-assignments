package spl;

import spl.services.ChatService;
import spl.services.ColorService;
import spl.services.MessageTransformer;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import spl.services.IdentityMessageTransformer;
import spl.services.ColorMessageTransformer;

import java.io.IOException;


public class ChatApp {

    private static ChatService chatService;
    private static MessageTransformer messageTransformer;


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
        messageTransformer = use ? new ColorMessageTransformer() : new IdentityMessageTransformer();
    }

    public static void main(String[] args) throws IOException {
        determineEncryptionMethod("rot13");
        useUsernameColor(true);
        new GUI(chatService, messageTransformer);
//        new CLI(chatService, messageTransformer);
    }


}
