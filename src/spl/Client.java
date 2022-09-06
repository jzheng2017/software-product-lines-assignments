package spl;

import java.net.*;
import java.io.*;
 

public class Client {
	
	String hostname;
	int port;
	Socket skt;
	
	String username;
	
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
    	try {
            OutputStream output = skt.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(message);
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
    
    public void disconnect() {
    	try {
    		skt.close();
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
}
