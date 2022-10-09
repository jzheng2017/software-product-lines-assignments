package spl; 

import spl.services.ChatService; 
import spl.services.ColorService; 
import spl.services.MessageTransformer; 
import spl.services.EncryptionServiceFactory; 
import spl.services.EncryptionType; 
import spl.services.FileLogService; 
import spl.services.IdentityMessageTransformer; 
import spl.services.ColorMessageTransformer; 
import spl.services.MessageTransformerFactory;

import java.io.IOException; 


public  class  ChatApp {

    public static void main(String[] args) throws IOException {
        new CLI(new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService()), MessageTransformerFactory.createMessageTransformer());
    }


}
