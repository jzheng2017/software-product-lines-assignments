package spl;

import spl.services.ChatService;
import spl.services.ConsoleLogService;
import spl.services.FileLogService;
import spl.services.LogService;
import spl.services.SimpleEncryptionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static final ChatService chatService = new ChatService(new FileLogService(), new SimpleEncryptionService());
    private static final LogService logger = new ConsoleLogService();

    static public void startServer(int port) {
        chatService.clearChatLogs();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.write("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                logger.write("New client connected");
                new ServerThread(socket).start();
            }

        } catch (IOException ex) {
            logger.write("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	//#if Logging
    	System.out.println("Logging Enabled");
    	//#else
//@    	System.out.println("Logging Disabled");
    	//#endif
        startServer(1234);
    }
}
