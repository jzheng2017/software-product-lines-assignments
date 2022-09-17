package spl;

import spl.services.ChatService;
import spl.services.EncryptionService;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import spl.services.ReverseStringEncryptionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static ChatService chatService;

    static public void startServer(int port) {
        chatService.clearChatLogs();

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket, chatService).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("rot13") || args[0].equals("reverse"))) {
            chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.toEnum(args[0])));
        } else {
            chatService = new ChatService(new FileLogService(), new EncryptionService() {
                @Override
                public String encrypt(String text) {
                    return text;
                }

                @Override
                public String decrypt(String text) {
                    return text;
                }
            });
        }
        startServer(1234);
    }
}
