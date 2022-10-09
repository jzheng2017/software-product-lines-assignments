package spl; 

import spl.services.ConsoleLogService; 
import spl.services.LogService; 
import spl.services.MessageSound;
import spl.services.ToolkitMessageSound;
import java.io.IOException; 
import java.io.OutputStream; 
import java.io.PrintWriter; 
import java.net.Socket; 
import java.net.UnknownHostException; 
import java.util.logging.Level; 
import java.util.logging.Logger; 


public  class  Client {
	
    private static final Logger logger = Logger.getLogger(Client.class.getName());

	
    public static boolean IS_AUTHENTICATED = false;

	
    private final String hostname;

	
    private final int port;

	
    private Socket skt;

	

    private final String username;

	
    private final LogService logService = new ConsoleLogService();
    private final MessageSound messageSound = new ToolkitMessageSound();
    

	

    public void connect() {
        try {
            skt = new Socket(hostname, port);
            Thread receiver_thread = new Thread(new ClientThread(skt));
            receiver_thread.start();

        } catch (UnknownHostException ex) {
            logService.write("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            logService.write("I/O error: " + ex.getMessage());
        }
    }

	

    public void sendMessage(String message) {
        if (IS_AUTHENTICATED || message.contains("/auth")) {
            try {
                OutputStream output = skt.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
                messageSound.beep();
            } catch (Exception e) {
                logService.write(e.getMessage());
            }
        } else {
            logger.log(Level.WARNING, "You are not authenticated to send messages");
        }
    }

	

    public void disconnect() {
    	try {
            logService.write("disconnected");
    		skt.close();
    		IS_AUTHENTICATED = false;
    	}
    	catch (Exception e) {
    		logService.write(e.getMessage());
    	}
    }


}
