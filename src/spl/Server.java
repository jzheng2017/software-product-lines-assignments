package spl;

import spl.services.ChatService;
import spl.services.FileLogService;
import spl.services.SimpleEncryptionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static final ChatService chatService = new ChatService(new FileLogService(), new SimpleEncryptionService());

    static public void startServer(int port) {
        chatService.clearChatLogs();

        try (ServerSocket serverSocket = new ServerSocket(port)) {

        	//#if Logging
//@            System.out.println("Server is listening on port " + port);
            //#endif
            while (true) {
                Socket socket = serverSocket.accept();
                //#if Logging
//@                System.out.println("New client connected");
                //#endif
                new ServerThread(socket).start();
            }

        } catch (IOException ex) {
        	//#if Logging
//@            System.out.println("Server exception: " + ex.getMessage());
            //#endif
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	//#if Logging
//@    	System.out.println("Logging Enabled");
    	//#else
    	System.out.println("Logging Disabled");
    	//#endif
        startServer(1234);
    }
}
