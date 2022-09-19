package spl;

import spl.services.ChatService;
import spl.services.ConsoleLogService;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import spl.services.LogService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static ChatService chatService;
    private static final LogService logger = new ConsoleLogService();

    static public void startServer(int port) {
        chatService.clearChatLogs();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.write("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                logger.write("New client connected");
                new ServerThread(socket, chatService).start();
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
        determineEncryptionMethod();
        startServer(1234);
    }

    private static void determineEncryptionMethod() {
        boolean encryptionDecided = false;

        //#if Rot13
//@        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.ROT13));
//@        encryptionDecided = true;
        //#endif

        //#if Reverse
//@        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.REVERSE));
//@        encryptionDecided = true;
        //#endif

        if (!encryptionDecided) {
            chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.PLAIN));
        }
    }
}
