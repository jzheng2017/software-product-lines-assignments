package spl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spl.services.FeatureConfigurationService;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private final FeatureConfigurationService fcs;
    public static boolean IS_AUTHENTICATED = false;
    private final String hostname;
    private final int port;
    private Socket skt;
    List<String> cmds = Arrays.asList("/auth");

    public Client(String hname, int prt, String user, FeatureConfigurationService fcs) {
    	this.fcs = fcs;
        hostname = hname;
        port = prt;
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
    
    public void readInput(String color, String message) {
    	String[] splitted = message.split("\\s+");
    	String command = splitted[0];
    	if(cmds.contains(command)) {
    		if(command.equals("/auth")) {
    	    	try {
    	            OutputStream output = skt.getOutputStream();
    	            PrintWriter writer = new PrintWriter(output, true);	
    	            writer.println(message);
    	        } catch (Exception e) {
    	            System.out.println(e);
    	        }
    		}
    	}
    	else if(IS_AUTHENTICATED) {
            sendMessage(color, message);
    	}
    	else {
    		logger.log(Level.WARNING, "You are not authenticated to send messages");
    	}
    }

    private void sendMessage(String color, String message) {
        try {
        	String messageTBS = (fcs.isFeatureOn("usernameColors") ? ("[" + color + "]: " + message) : message);
            OutputStream output = skt.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(messageTBS);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void disconnect() {
    	try {
            System.out.println("disconnected");
    		skt.close();
    		IS_AUTHENTICATED = false;
    	}
    	catch (Exception e) {
    		System.out.println(e);
    	}
    }
}
