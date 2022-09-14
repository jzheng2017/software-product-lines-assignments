package spl;

import spl.services.ChatService;
import spl.services.FeatureConfigurationService;
import spl.services.FileLogService;
import spl.services.ReverseStringEncryptionService;
import spl.services.Rot13EncryptionService;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Server {
    private static ChatService chatService;
    private static final FeatureConfigurationService fcs = new FeatureConfigurationService();

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
        List<String> allFeatures = Arrays.asList("reverse", "rot13", "usernamecolors");
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        for(String feat : allFeatures){
            fcs.addFeature(feat.toLowerCase(), arguments.contains(feat));
        }
        chatService = new ChatService(new FileLogService(), (fcs.isFeatureOn("rot13") ? new Rot13EncryptionService() : new ReverseStringEncryptionService()));
        startServer(1234);
    }
}
