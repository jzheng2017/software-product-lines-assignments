package spl; 

import spl.services.ChatService; 
import spl.services.MessageTransformer; 
import spl.services.EncryptionService; 
import spl.services.FileLogService; 

import java.io.IOException; 


public  class  ChatApp {
	

    private static ChatService chatService;

	
    private static MessageTransformer messageTransformer;

	

    public static void main(String[] args) throws IOException {
    	chatService = new ChatService(new FileLogService(), new EncryptionService());
    	messageTransformer = new MessageTransformer();
    	new UI(chatService, messageTransformer);
        //new GUI(chatService, messageTransformer);
		//new CLI(chatService, messageTransformer);
    }


}
