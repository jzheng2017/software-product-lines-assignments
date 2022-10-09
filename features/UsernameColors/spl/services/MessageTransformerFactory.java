package spl.services; 

public class MessageTransformerFactory {
	
    public static MessageTransformer createMessageTransformer() {
    	return new ColorMessageTransformer();
    }


}
