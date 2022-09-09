package spl;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    public static boolean IS_AUTHENTICATED = false;
    private String hostname;
	private int port;
	private Socket skt;
	
	private String username;
	public Client(String hname, int prt, String user) {
		hostname = hname;
		port = prt;
		
		username = user;
	}
 
    public void connect() {
        try {
        	skt = new Socket(hostname, port);
        	Thread receiver_thread = new Thread(new ClientThread(skt));
        	receiver_thread.start();
 
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
    
    public void sendMessage(String message) {
        if (IS_AUTHENTICATED || message.contains("/auth")) {
            try {
                OutputStream output = skt.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            logger.log(Level.WARNING, "You are not authenticated to send messages");
        }
    }
    
    public void disconnect() {
    	try {
    		IS_AUTHENTICATED = false;
            System.out.println("disconnected");
    		skt.close();
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
}
