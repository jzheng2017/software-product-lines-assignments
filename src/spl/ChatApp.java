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


public    class   ChatApp {
	
	

    private static ChatService chatService  ;

	

	
    private static MessageTransformer messageTransformer  ;

	
	
    public static void main  (String[] args) throws IOException {
        new GUI(chatService, messageTransformer);
//        new CLI(chatService, messageTransformer);
    }


}
