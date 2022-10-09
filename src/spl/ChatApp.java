package spl;  

import spl.services.ChatService;  
import spl.services.EncryptionServiceFactory;  
import spl.services.EncryptionType;  
import spl.services.FileLogService;  
import spl.services.MessageTransformerFactory; 

import java.io.IOException;  


public   class   ChatApp {
	

	
    public static void main(String[] args) throws IOException {
        new GUI(new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService()), MessageTransformerFactory.createMessageTransformer());
    }


}
