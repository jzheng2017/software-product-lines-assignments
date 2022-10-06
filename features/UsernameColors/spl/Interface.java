package spl;

import spl.services.ChatService;
import spl.services.MessageTransformer;
import spl.services.ColorService;
import spl.Interface;

public class Interface {
	
    public Interface(ChatService chatService, MessageTransformer messageTransformer)
    {
    	original(chatService, messageTransformer);
    	ColorService.getInstance().setColor(usernameColor);
    }
}