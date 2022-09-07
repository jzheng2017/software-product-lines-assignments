package spl;

import spl.services.ChatService;
import spl.services.FileLogService;
import spl.services.SimpleEncryptionService;

import java.io.*;
import java.net.*;
 

public class Server {
	
	static public void startServer(int port) {
        new ChatService(new FileLogService(), new SimpleEncryptionService()).clearChatLogs();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
 
                new ServerThread(socket).start();
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
 
    public static void main(String[] args) {
        startServer(1234);
    }
}
