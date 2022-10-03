package spl;  

import spl.services.ChatService;  
import spl.services.ColorService;  
import spl.services.MessageTransformer;  
import spl.services.ConsoleLogService;  
import spl.services.LogService;  

public abstract   class   Interface {
	
	
    protected final LogService logger = new ConsoleLogService();

	

	
    // Connect status constants
    protected final int DISCONNECTED = 0;

	

	
    protected final int BEGIN_CONNECT = 1;

	

	
    protected final int CONNECTED = 2;

	

	
    protected final ChatService chatService;

	

	
    protected final MessageTransformer messageTransformer;

	

	
    // Connection info
    protected String hostIP = "localhost";

	

	
    protected int port = 1234;

	

	
    protected int connectionStatus = DISCONNECTED;

	

	
    protected boolean isHost = true;

	

	
    protected Client user = new Client(hostIP, port, "Bob");

	

	
    protected String usernameColor = "Red";

	

	

    public Interface(ChatService chatService, MessageTransformer messageTransformer) {
        this.chatService = chatService;
        this.messageTransformer = messageTransformer;
        ColorService.getInstance().setColor(usernameColor);
        init();
        update();
    }

	

	

    public abstract void init();

	

	
    public abstract void update();


}
